package com.miu30.common.base;

import android.app.KeyguardManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.FragmentActivity;

import com.miu30.common.ui.widget.MyProgressDialog;

import java.lang.ref.SoftReference;

public class BaseActivity extends FragmentActivity {
    protected BaseActivity self;
    private MyProgressDialog waitDialog;

    private SoftReference<KeyguardManager.KeyguardLock> kl;
    private SoftReference<PowerManager.WakeLock> unLock;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        self = this;
    }

    protected void onDestroy() {
        super.onDestroy();
        if (waitDialog != null && waitDialog.isShowing()) {
            waitDialog.dismiss();
        }
    }

    public void setWaitDialog(MyProgressDialog waitDialog) {
        this.waitDialog = waitDialog;
    }

}
