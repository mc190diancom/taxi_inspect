package com.miu30.common.util;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

/**
 * 作者：wanglei on 2019/5/7.
 * 邮箱：forwlwork@gmail.com
 */
public abstract class PermissionManager {
    private Activity activity;

    protected PermissionManager(@NonNull Activity activity) {
        this.activity = activity;
    }

    @SuppressWarnings("all")
    public void requestPermissions(String... permissions) {
        new RxPermissions(activity).request(permissions)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            success();
                        } else {
                            failure();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        error(throwable);
                    }
                });
    }

    protected abstract void success();

    protected abstract void failure();

    protected abstract void error(Throwable throwable);

}
