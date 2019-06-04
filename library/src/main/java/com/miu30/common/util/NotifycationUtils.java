package com.miu30.common.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.ArrayMap;
import android.widget.RemoteViews;

import com.jess.arms.utils.ArmsUtils;
import com.miu30.common.MiuBaseApp;
import com.miu360.library.R;

import java.util.HashMap;
import java.util.Map;

import me.jessyan.progressmanager.ProgressListener;
import me.jessyan.progressmanager.ProgressManager;
import me.jessyan.progressmanager.body.ProgressInfo;
import timber.log.Timber;

public class NotifycationUtils {

    public static final String BASE = "INSPECT_PASSENGER";
    public static final String MSG_TYPE = "MSG_NOTICE";
    public static final String MSG_CHANNEL_ID = BASE + "_" + MSG_TYPE + "_CHANNEL_ID";
    public static final int MSG_NOTIFICATION_ID = MSG_CHANNEL_ID.hashCode();
    private Notification notification;
    private NotificationManager manager;
    private NotificationCompat.Builder builder;


    public void notificationMsg(String fileName) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            builder = new NotificationCompat.Builder(MiuBaseApp.self);
            builder.setSmallIcon(R.drawable.taxi_heade);
            builder.setContentTitle("下载" + fileName + "文件");
            builder.setContentText("正在准备");
            builder.setAutoCancel(false);
            builder.setChannelId(MiuBaseApp.self.getPackageName());
            manager = (NotificationManager) MiuBaseApp.self.getSystemService(Context.NOTIFICATION_SERVICE);
            notification = builder.build();
            notification.flags |= Notification.FLAG_NO_CLEAR;
            NotificationChannel channel = new NotificationChannel(
                    MiuBaseApp.self.getPackageName(),
                    "会话类型",//这块Android9.0分类的比较完整，你创建多个这样的东西，你可以在设置里边显示那个或者第几个
                    NotificationManager.IMPORTANCE_LOW

            );
            manager.createNotificationChannel(channel);
            manager.notify(MSG_NOTIFICATION_ID, notification);

        }else{
            builder = new NotificationCompat.Builder(MiuBaseApp.self);
            builder.setSmallIcon(R.drawable.taxi_heade);
            builder.setContentTitle("下载" + fileName + "文件");
            builder.setContentText("正在准备");
            builder.setAutoCancel(false);
            manager = (NotificationManager) MiuBaseApp.self.getSystemService(Context.NOTIFICATION_SERVICE);
            notification = builder.build();
            notification.flags |= Notification.FLAG_NO_CLEAR;
            manager.notify(MSG_NOTIFICATION_ID, notification);
        }

    }

    public void NotifycationUtils(String tagUrl, String fileName) {
        notificationMsg(fileName);
        ProgressManager.getInstance().addResponseListener(tagUrl, new ProgressListener() {
            @Override
            public void onProgress(ProgressInfo progressInfo) {
                String s = progressInfo.toString();
                Timber.tag("download").i(s);
                System.out.println("download:"+s);
                if (progressInfo.getContentLength() > 0) {
                    builder.setProgress((int) progressInfo.getContentLength(), (int) progressInfo.getCurrentbytes(), false);
                    //下载进度提示
                    double l = ((double) progressInfo.getCurrentbytes() + 0.0) / ((double) progressInfo.getContentLength() + 0.0);

                    builder.setContentText("下载" + String.format("%.2f", (l * 100)) + "%");
                    if (progressInfo.isFinish()) {
                        builder.setContentTitle("下载完成");
                    }
                    manager.notify(MSG_NOTIFICATION_ID, notification = builder.build());
                } else if(progressInfo.getContentLength() == -1){
                    System.out.println("download:取消");
                    manager.cancelAll();
                }else{
                    builder.setProgress(0, 0, true);
                    manager.notify(MSG_NOTIFICATION_ID, notification = builder.build());
                }

            }

            @Override
            public void onError(long id, Exception e) {
                builder.setContentTitle("下载失败");
                manager.notify(MSG_NOTIFICATION_ID, notification = builder.build());
                Timber.tag("download_error").i(id + "");
                e.printStackTrace();
            }
        });
    }

    public static NotifycationUtils create() {
        return new NotifycationUtils();
    }


}
