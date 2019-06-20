package com.feidi.video.util;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.ToastUtils;
import com.feidi.video.R;
import com.miu30.common.util.Windows;

/**
 * 作者：wanglei on 2019/6/19.
 * 邮箱：forwlwork@gmail.com
 */
public class DialogUtil {

    //----------------------------------------------------------------------------------------------

    public interface OnRemoveReasonListener {
        void onRemoveReason(String reason);
    }

    /**
     * 显示位于屏幕底部的移除可疑车辆库的Dialog
     *
     * @param context 上下文
     */
    public static void showRemoveDoubtCarDialog(Context context, final OnRemoveReasonListener listener) {
        final Dialog dialog = Windows.createBottomDialog(context,R.layout.dialog_remove_doubt_car);
        dialog.setCanceledOnTouchOutside(false);
        dialog.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        final Button btnConfirmRemove = dialog.findViewById(R.id.btn_confirm_remove);
        final EditText etContent = dialog.findViewById(R.id.et_content);
        btnConfirmRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reason = etContent.getText().toString().trim();
                if (!TextUtils.isEmpty(reason) && listener != null) {
                    listener.onRemoveReason(reason);
                    dialog.dismiss();
                } else {
                    ToastUtils.showShort("请输入移除原因");
                }
            }
        });
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean isEnabled = btnConfirmRemove.isEnabled();
                if (TextUtils.isEmpty(s)) {
                    if (isEnabled) {
                        btnConfirmRemove.setEnabled(false);
                    }
                } else {
                    if (!isEnabled) {
                        btnConfirmRemove.setEnabled(true);
                    }
                }
            }
        });

        dialog.show();
    }

    //----------------------------------------------------------------------------------------------

}
