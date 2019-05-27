package com.miu360.legworkwrit.mvp.ui.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.view.View;

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
        receiver = new BroadcastReceiver() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onReceive(Context context, Intent intent) {
                if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                    int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                    switch (state) {
                        case WifiManager.WIFI_STATE_ENABLED:
                            tree.i("wifi 已打开");
                            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                                tree.i("未获取到定位权限");
                            } else {
                                tree.i("已获取到定位权限");
                            }

                            scan();
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
                }
            }
        };
        registerReceiver(receiver, filter);

        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1.打开wifi
                if (!WifiUtil.getInstance().isOpen()) {
                    WifiUtil.getInstance().open();
                } else {
                    scan();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    private void scan() {
        tree.i("当前连接的wifi : %s ", WifiUtil.getInstance().getConnectInfo().getSSID());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                WifiUtil.getInstance().scan();
            }
        }, 2000);

        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        startActivity(intent);

        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));

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
