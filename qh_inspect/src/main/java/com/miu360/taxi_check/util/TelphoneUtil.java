package com.miu360.taxi_check.util;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.miu360.taxi_check.App;

/**
 * 作者：wanglei on 2018/8/23.
 * 邮箱：forwlwork@gmail.com
 */

public class TelphoneUtil {

    private TelphoneUtil() {
        throw new IllegalStateException("no instance.");
    }

    public static String getTelphoneNumber() {
        TelephonyManager manager = (TelephonyManager) App.self.getSystemService(Context.TELEPHONY_SERVICE);

        if (manager != null) {
            return "" + manager.getLine1Number();
        }

        return "";
    }

    public static String getIMEI() {
        TelephonyManager manager = (TelephonyManager) App.self.getSystemService(Context.TELEPHONY_SERVICE);

        if (manager != null) {
            return "" + manager.getDeviceId();
        }

        return "";
    }

}
