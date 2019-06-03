package com.miu360.legworkwrit.mvp.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;

import com.miu360.legworkwrit.R;
import com.miu360.legworkwrit.util.WifiUtil;
import com.miu360.legworkwrit.util.printer.PrinterManager;

import java.util.List;

import timber.log.Timber;

/**
 * 作者：wanglei on 2019/5/27.
 * 邮箱：forwlwork@gmail.com
 * <p>
 * 主要测试能否连接到指定wifi（已知wifi的ssid、password）
 */
public class WifiTestActivity extends FragmentActivity {
    private static final String TAG = PrinterManager.TAG;

    private BroadcastReceiver receiver;

    private Timber.Tree tree;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_test);

        tree = Timber.tag(TAG);

        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        receiver = new BroadcastReceiver() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onReceive(Context context, Intent intent) {
                if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                    int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                    switch (state) {
                        case WifiManager.WIFI_STATE_ENABLED:
                            tree.i("wifi 已打开");
//                            scan();
                            //开始扫描
                            WifiUtil.getInstance().scan();

//                            handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                }
//                            }, 2000);
                            break;
                        case WifiManager.WIFI_STATE_ENABLING:
                            tree.i("wifi 正在打开中 .....");
                            break;
                    }
                } else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                    NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                    if (NetworkInfo.State.DISCONNECTED.equals(info.getState())) {
                        tree.i("wifi 已断开");
                    } else if (NetworkInfo.State.DISCONNECTING.equals(info.getState())) {
                        tree.i("正在断开wifi ....");
                    } else if (NetworkInfo.State.CONNECTED.equals(info.getState())) {
                        tree.i("已连接到wifi : %s", WifiUtil.getInstance().getConnectInfo().getSSID());
                    } else if (NetworkInfo.State.CONNECTING.equals(info.getState())) {
                        tree.i("正在连接到wifi ....");
                    }
                } else if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())) {
                    tree.i("接收到已搜索到周边wifi的广播");
                    List<ScanResult> result = WifiUtil.getInstance().getScanResult();

                    if (result != null && !result.isEmpty()) {
                        for (ScanResult s : result) {
                            tree.i("搜索到wifi : %s", s.SSID);
                            if ("android".equals(s.SSID)) {
                                WifiUtil.getInstance()
                                        .connect(WifiUtil.getInstance().createWifiInfo(s.SSID, "333666999", 3));
                                break;
                            }
                        }
                    } else {
                        tree.i("周围没有可用wifi");
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                WifiUtil.getInstance().scan();
                            }
                        }, 2000);
                    }
                }
            }
        };
        registerReceiver(receiver, filter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!WifiUtil.getInstance().isOpen()) {
                    WifiUtil.getInstance().open();
                }
            }
        }).start();
    }

    private void scan() {
        tree.i("当前连接的wifi : %s ", WifiUtil.getInstance().getConnectInfo().getSSID());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                WifiUtil.getInstance().scan();
            }
        }, 2000);

        // startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));

        handler.postDelayed(runnable, 5000);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            List<ScanResult> result = WifiUtil.getInstance().getScanResult();

            if (result != null && !result.isEmpty()) {
                for (ScanResult s : result) {
                    tree.i("搜索到wifi : %s", s.SSID);
                    if ("fucku".equals(s.SSID)) {
                        WifiUtil.getInstance()
                                .connect(WifiUtil.getInstance().createWifiInfo(s.SSID, "12345678", 3));
                        break;
                    }
                }
            } else {
                tree.i("周围没有可用wifi");
                handler.postDelayed(this, 2000);
            }
        }
    };

    @Override
    protected void onDestroy() {
        //关闭wifi
        if (WifiUtil.getInstance().isOpen()) {
            tree.i("准备关闭wifi");
            WifiUtil.getInstance().close();
        }

        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        super.onDestroy();
    }
}
