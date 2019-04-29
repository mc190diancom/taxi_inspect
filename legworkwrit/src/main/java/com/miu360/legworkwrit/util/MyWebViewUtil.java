package com.miu360.legworkwrit.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.miu30.common.config.Config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MyWebViewUtil {

    public static WebView getPreViewWebView(Activity context, @IdRes int id) {
        final WebView webView = (WebView) context.findViewById(id);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDefaultTextEncodingName("utf-8");
        //支持屏幕缩放
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        return webView;
    }

    public static String saveViewToImage(View view) {
        if (view == null) {
            return null;
        }
        int width = view.getWidth();
        int height = 1528;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        FileOutputStream fos = null;
        try {
            File outputFile = new File(Environment.getExternalStorageDirectory(), "test.jpg");
            fos = new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.flush();
            return outputFile.getPath();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    /**
     * 保存图片
     */
    public static String saveImage(WebView webView) {
        Picture picture = webView.capturePicture();
        Bitmap b = Bitmap.createBitmap(
                1080, 1527, Bitmap.Config.ARGB_8888);
        System.out.println("capturePicture:" + picture.getWidth() + "===" + picture.getHeight());
        Canvas c = new Canvas(b);
        picture.draw(c);
        File file = new File(Environment.getExternalStorageDirectory(), "test.jpg");
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file.getAbsoluteFile());
            if (fos != null) {
                b.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    public interface OnScreenshotListener {
        void onScreenshot(File capture);
    }

    @SuppressWarnings("all")
    public static void getScreenshot(WebView webView, final String fileName, final OnScreenshotListener listener) {
        Observable.just(webView)
                .flatMap(new Function<WebView, ObservableSource<Bitmap>>() {
                    @Override
                    public ObservableSource<Bitmap> apply(WebView webView) throws Exception {
                        Picture picture = webView.capturePicture();
                        Bitmap bitmap;
                        bitmap = Bitmap.createBitmap(picture.getWidth(), picture.getHeight(), Bitmap.Config.ARGB_8888);
                        /*if (Build.VERSION.SDK_INT >= 22) {
                            bitmap = Bitmap.createBitmap(2382, 3369, Bitmap.Config.ARGB_8888);
                        } else {
                            bitmap = Bitmap.createBitmap(2200, 3110, Bitmap.Config.ARGB_8888);
                        }*/
                        Canvas canvas = new Canvas(bitmap);
                        canvas.drawColor(Color.TRANSPARENT);
                        Paint paint = new Paint();
                        canvas.drawBitmap(bitmap, 0, 0, paint);
                        webView.draw(canvas);
                        return Observable.just(bitmap);
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())

                .observeOn(Schedulers.io())
                .flatMap(new Function<Bitmap, ObservableSource<File>>() {
                    @Override
                    public ObservableSource<File> apply(Bitmap bitmap) throws Exception {
                        File dir = new File(Config.PATH);
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        File capture = new File(dir, fileName);
                        if (capture.exists()) {
                            capture.delete();
                        }

                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(capture);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            fos.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return Observable.just(capture);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(File file) throws Exception {
                        if (listener != null) {
                            listener.onScreenshot(file);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("error", throwable.toString());
                    }
                });

    }

    public static File getFullWebViewSnapshot(WebView webView, String fileName) {
        //开启WebView的缓存(当开启这个开关后下次调用getDrawingCache()方法的时候会把view绘制到一个bitmap上)
        webView.setDrawingCacheEnabled(true);
        //强制绘制缓存（必须在setDrawingCacheEnabled(true)之后才能调用，否者需要手动调用destroyDrawingCache()清楚缓存）
        webView.buildDrawingCache();
        //根据测量结果创建一个大小一样的bitmap
        Bitmap picture;
        if (Build.VERSION.SDK_INT >= 22) {
            picture = Bitmap.createBitmap(2385, 3386, Bitmap.Config.ARGB_8888);
        } else {
            picture = Bitmap.createBitmap(2200, 3200, Bitmap.Config.ARGB_8888);
        }

        //已picture为背景创建一个画布
        Canvas canvas = new Canvas(picture);  // 画布的宽高和 WebView 的网页保持一致
        canvas.drawColor(Color.TRANSPARENT);
        Paint paint = new Paint();
        //设置画笔的定点位置，也就是左上角
        canvas.drawBitmap(picture, 0, 0, paint);
        //将webview绘制在刚才创建的画板上
        webView.draw(canvas);

        File parentFile = new File(Config.PATH);
        parentFile.mkdirs();
        File file = new File(parentFile, fileName);
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file.getAbsoluteFile());
            picture.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }

    public static File getFullWebViewSnapshot2(View webView, String fileName) {
        //重新调用WebView的measure方法测量实际View的大小（将测量模式设置为UNSPECIFIED模式也就是需要多大就可以获得多大的空间）
        webView.measure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        //调用layout方法设置布局（使用新测量的大小）
        webView.layout(0, 0, webView.getMeasuredWidth() * 2, webView.getMeasuredHeight() * 2);
        //开启WebView的缓存(当开启这个开关后下次调用getDrawingCache()方法的时候会把view绘制到一个bitmap上)
        webView.setDrawingCacheEnabled(true);
        //强制绘制缓存（必须在setDrawingCacheEnabled(true)之后才能调用，否者需要手动调用destroyDrawingCache()清楚缓存）
        webView.buildDrawingCache();
        //根据测量结果创建一个大小一样的bitmap
        Bitmap picture = Bitmap.createBitmap(webView.getMeasuredWidth() * 2,
                webView.getMeasuredHeight() * 2, Bitmap.Config.ARGB_8888);
        //已picture为背景创建一个画布
        Canvas canvas = new Canvas(picture);  // 画布的宽高和 WebView 的网页保持一致
        Paint paint = new Paint();
        //设置画笔的定点位置，也就是左上角
        canvas.drawBitmap(picture, 0, webView.getMeasuredHeight(), paint);
        //将webview绘制在刚才创建的画板上
        webView.draw(canvas);
        File parentFile = new File(Config.PATH);
        parentFile.mkdirs();
        File file = new File(parentFile, fileName);
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file.getAbsoluteFile());
            picture.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

}
