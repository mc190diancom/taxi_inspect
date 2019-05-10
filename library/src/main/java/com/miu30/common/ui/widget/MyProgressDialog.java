package com.miu30.common.ui.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

import com.miu30.common.base.BaseActivity;


public abstract class MyProgressDialog extends Dialog {
    private Context context;
    private Dialog self;

    public MyProgressDialog(Context context, int layout, int style) {
        super(context, style);
        setContentView(layout);
        this.context = context;
        self = this;
        Window window = getWindow();
        if (!(context instanceof Activity)) {
            window.setType(LayoutParams.TYPE_SYSTEM_ALERT);
        }
        LayoutParams params = window.getAttributes();
        params.width = LayoutParams.WRAP_CONTENT;
        params.height = LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);

        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    public abstract void showListener();

    public abstract void closeListener();

    public void closeDialog() {
        self.dismiss();
        closeListener();
    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
            if (context instanceof BaseActivity) {
                ((BaseActivity) context).setWaitDialog(null);
            }
            closeListener();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void show() {
        boolean show = false;
        if (context instanceof Activity) {
            if (!((Activity) context).isFinishing()) {
                show = true;
            }
        } else {
            show = true;
        }
        if (show) {
            super.show();
            if (context instanceof BaseActivity) {
                ((BaseActivity) context).setWaitDialog(this);
            }
            showListener();
        }
    }
}
