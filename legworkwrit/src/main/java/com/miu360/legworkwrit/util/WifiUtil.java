package com.miu360.legworkwrit.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import com.miu30.common.MiuBaseApp;
import com.miu360.legworkwrit.mvp.model.entity.WifiConfig;
import com.miu360.legworkwrit.util.printer.PrinterManager;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 作者：wanglei on 2018/10/22.
 * 邮箱：forwlwork@gmail.com
 */
@SuppressLint("LogNotTimber")
public class WifiUtil {
    private WifiManager manager;

    private WifiUtil() {
        manager = (WifiManager) MiuBaseApp.self.getSystemService(Context.WIFI_SERVICE);
    }

    private static class Holder {
        private static final WifiUtil INSTANCE = new WifiUtil();
    }

    public static WifiUtil getInstance() {
        return Holder.INSTANCE;
    }

    public boolean isOpen() {
        return manager.isWifiEnabled();
    }

    public void open() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                manager.setWifiEnabled(true);
            }
        }).start();
    }

    public void close() {
       // if (manager.isWifiEnabled()) {
            manager.setWifiEnabled(false);
        //}
    }

    public void scan() {
        Log.d(PrinterManager.TAG, "开始搜索wifi");
        manager.startScan();
    }

    public List<ScanResult> getScanResult() {
        return manager.getScanResults();
    }

    private WifiConfiguration isExsits(String SSID) {
        List<WifiConfiguration> existingConfigs = manager.getConfiguredNetworks();

        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                return existingConfig;
            }
        }

        return null;
    }

    public WifiConfiguration createWifiInfo(String SSID, String Password, int Type) {
        Log.d(PrinterManager.TAG
                , "createWifiInfo....SSID = " + SSID + "....Password = " + Password + "....Type = " + Type);

        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";

        WifiConfiguration tempConfig = this.isExsits(SSID);
        if (tempConfig != null) {
            manager.removeNetwork(tempConfig.networkId);
        }

        if (Type == 1) {
            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }

        if (Type == 2) {
            config.hiddenSSID = true;
            config.wepKeys[0] = "\"" + Password + "\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }

        if (Type == 3) {
            config.preSharedKey = "\"" + Password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);//WPA_PSK
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;

            /*config.status = WifiConfiguration.Status.ENABLED;
            config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.preSharedKey = "\"".concat(Password).concat("\"");*/

        }

        config.priority = 10000;

        return config;
    }

    public WifiInfo getConnectInfo() {
        return manager.getConnectionInfo();
    }

    public void disConnect() {
        manager.disconnect();
    }

    public void connect(WifiConfiguration configuration) {
        Log.d(PrinterManager.TAG, "准备连接到wifi.....ssid = " + configuration);

        int netId = manager.addNetwork(configuration);

        Log.d(PrinterManager.TAG, "addNetwork , netId = " + netId);

        for (WifiConfiguration c : manager.getConfiguredNetworks()) {
            if (c.networkId != netId) {
                manager.disableNetwork(netId);
            }
        }

        Method method = connectWifiByReflectMethod(netId);

        if (method == null) {
            boolean result = manager.enableNetwork(netId, true);
            Log.d(PrinterManager.TAG, "netId = " + netId + " ... result = " + result);
        }

    }

    private Method connectWifiByReflectMethod(int netId) {
        Method connectMethod = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Log.i(PrinterManager.TAG, "connectWifiByReflectMethod road 1");
            // 反射方法： connect(int, listener) , 4.2 <= phone‘s android version
            for (Method methodSub : manager.getClass()
                    .getDeclaredMethods()) {
                if ("connect".equalsIgnoreCase(methodSub.getName())) {
                    Class<?>[] types = methodSub.getParameterTypes();
                    if (types != null && types.length > 0) {
                        if ("int".equalsIgnoreCase(types[0].getName())) {
                            connectMethod = methodSub;
                        }
                    }
                }
            }
            if (connectMethod != null) {
                try {
                    connectMethod.invoke(manager, netId, null);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i(PrinterManager.TAG, "connectWifiByReflectMethod Android "
                            + Build.VERSION.SDK_INT + " error!");
                    return null;
                }
            }
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN) {
            // 反射方法: connect(Channel c, int networkId, ActionListener listener)
            // 暂时不处理4.1的情况 , 4.1 == phone‘s android version
            Log.i(PrinterManager.TAG, "connectWifiByReflectMethod road 2");
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            Log.i(PrinterManager.TAG, "connectWifiByReflectMethod road 3");
            // 反射方法：connectNetwork(int networkId) ,
            // 4.0 <= phone‘s android version < 4.1
            for (Method methodSub : manager.getClass()
                    .getDeclaredMethods()) {
                if ("connectNetwork".equalsIgnoreCase(methodSub.getName())) {
                    Class<?>[] types = methodSub.getParameterTypes();
                    if (types != null && types.length > 0) {
                        if ("int".equalsIgnoreCase(types[0].getName())) {
                            connectMethod = methodSub;
                        }
                    }
                }
            }
            if (connectMethod != null) {
                try {
                    connectMethod.invoke(manager, netId);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i(PrinterManager.TAG, "connectWifiByReflectMethod Android "
                            + Build.VERSION.SDK_INT + " error!");
                    return null;
                }
            }
        } else {
            // < android 4.0
            return null;
        }
        return connectMethod;
    }

}
