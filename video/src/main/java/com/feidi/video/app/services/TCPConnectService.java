package com.feidi.video.app.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.support.annotation.Nullable;

import com.miu30.common.MiuBaseApp;
import com.miu30.common.config.Config;
import com.miu30.common.connect.ChannelManager;
import com.miu30.common.connect.NettyClient;
import com.miu30.common.connect.entity.CancelBindCameraRequest;
import com.miu30.common.connect.entity.LogoutRequest;

import timber.log.Timber;

/**
 * 作者：wanglei on 2019/5/23.
 * 邮箱：forwlwork@gmail.com
 */
public class TCPConnectService extends Service {
    private NettyClient client;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                if (client == null) {
                    client = new NettyClient();
                    client.connect(Config.IP, 9000);
                }
            }
        }).start();

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Timber.tag("netty").i("注销服务");
        if(ChannelManager.getInstance().isConnected()){
            ChannelManager.getInstance().sendMessage(new LogoutRequest(MiuBaseApp.user.getString("user_name", "0000000")));
        }
        if (client != null) {
            client.stop();
            client = null;
        }
        super.onDestroy();
    }
}
