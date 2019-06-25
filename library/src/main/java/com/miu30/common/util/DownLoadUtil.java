package com.miu30.common.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.LruCache;

import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;
import com.miu30.common.api.DownLoadService;
import com.miu30.common.config.Config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import okhttp3.ResponseBody;
import timber.log.Timber;

public class DownLoadUtil {

    private static final String SUFFIX = "_downloadFileCache";

    @SuppressLint("CheckResult")
    public static void downLoadFile(final Context context, final String path, String url) {
        if (TextUtils.isEmpty(path) || TextUtils.isEmpty(url)) {
            return;
        }
        disposableCancel(context, path);
        ArmsUtils.obtainAppComponentFromContext(context)
                .repositoryManager()
                .obtainRetrofitService(DownLoadService.class)
                .downloadFile(url)
                .map(new Function<ResponseBody, File>() {
                    @Override
                    public File apply(ResponseBody responseBody) throws Exception {
                        InputStream inputStream = responseBody.byteStream();
                        File file = new File(path);
                        if(!file.getParentFile().exists()){
                            file.getParentFile().mkdirs();
                        }
                        if(!file.exists()){
                            file.createNewFile();
                        }
                        FileOutputStream fileOutputStream = new FileOutputStream(path);
                        byte[] bytes = new byte[1024 * 10];
                        int len = 0;
                        while ((len = inputStream.read(bytes)) != -1) {
                            fileOutputStream.write(bytes, 0, len);
                        }
                        return file;
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorHandleSubscriber<File>(ArmsUtils.obtainAppComponentFromContext(context).rxErrorHandler()) {
            @Override
            public void onSubscribe(Disposable d) {
                putDownLoadRequest(context, path, d);
            }

            @Override
            public void onNext(File responseBody) {
                if (responseBody.exists()) {
                    Timber.tag("download_error").i("下载成功=-======");
                    checkMapCfg(context);
                    Cache<String, Object> extras = ArmsUtils.obtainAppComponentFromContext(context).extras();
                    extras.remove(path + SUFFIX);
                }
            }


        });
    }

    /*
     * 检测地图的配置信息是否存在和大小是否正确，否则进行拷贝
     */
    private static void checkMapCfg(final Context context) {
        final int version = Build.VERSION.SDK_INT;
        final String pathRoot = Environment.getExternalStorageDirectory().getAbsolutePath();
        final File filecfg = new File(pathRoot + "/Android/data/" + context.getPackageName() + "/files/BaiduMapSDKNew/vmp/DVUserdat.cfg");
        final File tempFilecfg = new File(pathRoot + "/BaiduMapSDKNew/vmp/DVUserdat.cfg");
        Observable.create(new ObservableOnSubscribe<Void>() { //定义被观察者
            @Override
            public void subscribe(final ObservableEmitter<Void> e){
                if(!filecfg.exists() && version >= Build.VERSION_CODES.LOLLIPOP || version >=Build.VERSION_CODES.LOLLIPOP && filecfg.exists() && filecfg.length() <1000){
                    FileUtil.copyAssrert2Sd(context,"BaiduMapSDKNew/vmp/DVUserdat.cfg", filecfg.getAbsolutePath());
                }
                if(version < Build.VERSION_CODES.LOLLIPOP && !tempFilecfg.exists() || version < Build.VERSION_CODES.LOLLIPOP && tempFilecfg.exists() && tempFilecfg.length() <1000 ){
                    FileUtil.copyAssrert2Sd(context,"BaiduMapSDKNew/vmp/DVUserdat.cfg", tempFilecfg.getAbsolutePath());
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    /**
     * 放入一个请求
     *
     * @param context
     * @param childName
     * @param object
     */
    public static void putDownLoadRequest(Context context, String childName, Object object) {
        ArmsUtils.obtainAppComponentFromContext(context).extras().put(childName + SUFFIX, object);
    }

    /**
     * 取消请求
     *
     * @param context
     * @param childName
     */
    public static void disposableCancel(Context context, String childName) {
        Cache<String, Object> extras = ArmsUtils.obtainAppComponentFromContext(context).extras();
        Object d = extras.get(childName + SUFFIX);
        if (d != null && d instanceof Disposable) {
            Disposable disposable = (Disposable) d;
            disposable.dispose();
            extras.remove(childName + SUFFIX);
        }
    }


}
