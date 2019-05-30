package com.feidi.video.app.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.support.annotation.Nullable;

import com.miu30.common.connect.NettyClient;

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
                    client.connect("bjqhcal.nat123.cc", 9000);
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
        if (client != null) {
            client.stop();
            client = null;
        }
        super.onDestroy();
    }
}
