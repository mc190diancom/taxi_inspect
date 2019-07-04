package android.print;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.util.Log;
import android.webkit.WebView;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者：wang.
 * 邮箱：forwlwork@gmail.com
 */
@SuppressWarnings("all")
public class PdfPrint {

    public static abstract class Callback {
        public void onStart() {

        }

        public abstract void onSuccess(File pdfFile);

        public abstract void onFailure(String message);

        public void onFinish() {

        }
    }

    private static final String TAG = PdfPrint.class.getSimpleName();
    private static final int DEFAULT_DELAY_BELOW_KITKAT = 500;
    private PrintAttributes printAttributes = null;

    private Handler handler = new Handler(Looper.getMainLooper());
    private Disposable disposable = null;

    public PdfPrint() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.printAttributes = new PrintAttributes.Builder()
                    .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                    .setResolution(new PrintAttributes.Resolution("pdf", "pdf", 600, 600))
                    .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                    .build();
        }
    }

    public PdfPrint(@NonNull PrintAttributes printAttributes) {
        this.printAttributes = printAttributes;
    }

    //@RequiresPermission(value = Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void printPdfFromWebView(@NonNull final WebView webView, @NonNull final File parentFile, @NonNull final String fileName, @Nullable final Callback callback) {
        if (callback != null) {
            callback.onStart();
        }

        if (!parentFile.isDirectory()) {
            if (callback != null) {
                callback.onFailure("parent file is must be directory.");
                callback.onFinish();
            }
            return;
        }

        //检查权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (webView.getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (callback != null) {
                    callback.onFailure("no permission : WRITE_EXTERNAL_STORAGE");
                    callback.onFinish();
                }
                return;
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            printPdfAboveKITKAT(webView, parentFile, fileName, callback);
        } else {
            printPdfBelowKITKAT(webView, parentFile, fileName, callback);
        }
    }

    private void printPdfAboveKITKAT(WebView webView, final File parentFile, final String fileName, final Callback callback) {
        final PrintDocumentAdapter adapter = webView.createPrintDocumentAdapter();
        adapter.onLayout(null, printAttributes, null, new PrintDocumentAdapter.LayoutResultCallback() {
            @Override
            public void onLayoutFinished(PrintDocumentInfo info, boolean changed) {
                adapter.onWrite(new PageRange[]{PageRange.ALL_PAGES}
                        , getOutputFile(parentFile, fileName)
                        , new CancellationSignal()
                        , new PrintDocumentAdapter.WriteResultCallback() {
                            @Override
                            public void onWriteFinished(PageRange[] pages) {
                                super.onWriteFinished(pages);
                                if (pages != null && pages.length > 0) {
                                    if (callback != null) {
                                        callback.onSuccess(new File(parentFile, fileName));
                                    }
                                } else {
                                    if (callback != null) {
                                        callback.onFailure("print pdf file failure.");
                                    }
                                }

                                if (callback != null) {
                                    callback.onFinish();
                                }
                            }
                        });
            }
        }, null);
    }

    private void printPdfBelowKITKAT(final WebView webView, final File parentFile, final String fileName, final Callback callback) {
        webView.getSettings().setUseWideViewPort(false);
        webView.getSettings().setLoadWithOverviewMode(false);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                disposable = Observable.just(webView)
                        .flatMap(new Function<WebView, ObservableSource<Bitmap>>() {
                            @Override
                            public ObservableSource<Bitmap> apply(WebView webView) throws Exception {
                                return Observable.just(getBitmapFromWebView(webView));
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(Schedulers.io())
                        .flatMap(new Function<Bitmap, ObservableSource<File>>() {
                            @Override
                            public ObservableSource<File> apply(Bitmap bitmap) throws Exception {
                                return Observable.just(bitmapToPdfFile(bitmap, new File(parentFile, fileName)));

                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(new Action() {
                            @Override
                            public void run() throws Exception {
                                if (callback != null) {
                                    callback.onFinish();
                                }
                            }
                        })
                        .subscribe(new Consumer<File>() {
                            @Override
                            public void accept(File file) throws Exception {
                                if (callback != null) {
                                    callback.onSuccess(file);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                if (callback != null) {
                                    callback.onFailure(throwable.toString());
                                }
                            }
                        });
            }
        }, DEFAULT_DELAY_BELOW_KITKAT);
    }

    private Bitmap getBitmapFromWebView(WebView webView) {
        Picture picture = webView.capturePicture();
        int width = picture.getWidth();
        int height = (int) (width * 1.414);//A4纸的高、宽比例为1.414

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.TRANSPARENT);
        Paint paint = new Paint();
        canvas.drawBitmap(bitmap, 0, 0, paint);
        webView.draw(canvas);

        return bitmap;
    }

    private File bitmapToPdfFile(Bitmap bitmap, File outputFile) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] byteArray = bos.toByteArray();
        bos.close();

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(outputFile));
        document.open();
        document.add(Image.getInstance(byteArray));
        document.close();

        return outputFile;
    }


    private ParcelFileDescriptor getOutputFile(File path, String fileName) {
        if (!path.exists()) {
            //noinspection ResultOfMethodCallIgnored
            path.mkdirs();
        }
        File file = new File(path, fileName);
        try {
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();
            return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_WRITE);
        } catch (Exception e) {
            Log.e(TAG, "Failed to open ParcelFileDescriptor", e);
        }
        return null;
    }

    public void destroy() {
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
    }

}
