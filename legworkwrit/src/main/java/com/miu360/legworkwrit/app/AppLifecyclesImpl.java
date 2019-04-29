package com.miu360.legworkwrit.app;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.os.Message;
import android.support.annotation.NonNull;

import com.jess.arms.base.delegate.AppLifecycles;

import butterknife.ButterKnife;

import com.jess.arms.integration.AppManager;
import com.jess.arms.utils.ArmsUtils;
import com.miu30.common.config.Config;
import com.miu30.common.util.DownLoadUtil;
import com.miu30.common.util.NotifycationUtils;
import com.miu360.legworkwrit.BuildConfig;

import java.io.File;
import java.io.IOException;

import timber.log.Timber;

/**
 * ================================================
 * 展示 {@link AppLifecycles} 的用法
 * <p>
 * Created by MVPArmsTemplate
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class AppLifecyclesImpl implements AppLifecycles {


    @Override
    public void attachBaseContext(@NonNull Context base) {
//          MultiDex.install(base);  //这里比 onCreate 先执行,常用于 MultiDex 初始化,插件化框架的初始化
    }

    @Override
    public void onCreate(@NonNull final Application application) {
        /*if (LeakCanary.isInAnalyzerProcess(application)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }*/
        if (BuildConfig.LOG_DEBUG) {//Timber初始化
            //Timber 是一个日志框架容器,外部使用统一的Api,内部可以动态的切换成任何日志框架(打印策略)进行日志打印
            //并且支持添加多个日志框架(打印策略),做到外部调用一次 Api,内部却可以做到同时使用多个策略
            //比如添加三个策略,一个打印日志,一个将日志保存本地,一个将日志上传服务器
            Timber.plant(new Timber.DebugTree());
            // 如果你想将框架切换为 Logger 来打印日志,请使用下面的代码,如想切换为其他日志框架请根据下面的方式扩展
//                    Logger.addLogAdapter(new AndroidLogAdapter());
//                    Timber.plant(new Timber.DebugTree() {
//                        @Override
//                        protected void log(int priority, String tag, String message, Throwable t) {
//                            Logger.log(priority, tag, message, t);
//                        }
//                    });
            ButterKnife.setDebug(true);
        }
        //LeakCanary 内存泄露检查
        //ArmsUtils.obtainAppComponentFromContext(application).extras().put(RefWatcher.class.getName(), BuildConfig.USE_CANARY ? LeakCanary.install(application) : RefWatcher.DISABLED);
        //扩展 AppManager 的远程遥控功能
        ArmsUtils.obtainAppComponentFromContext(application).appManager().setHandleListener(new AppManager.HandleListener() {
            @Override
            public void handleMessage(AppManager appManager, Message message) {
                switch (message.what) {
                    case EventBusTags.DOWNLOAD_MAP:
                        String url = new String(Config.SERVER_DOWNLOAD + "?type=downloadMaxVersionMap");
                        NotifycationUtils.create().NotifycationUtils(url, "百度离线地图");
                        String path = (String)message.obj;
                        DownLoadUtil.downLoadFile(appManager.getTopActivity(),path, url);
                        break;
                }
            }
        });
        //Usage:
        //Message msg = new Message();
        //msg.what = 0;
        //AppManager.post(msg); like EventBus

    }

    @Override
    public void onTerminate(Application application) {

    }


}
