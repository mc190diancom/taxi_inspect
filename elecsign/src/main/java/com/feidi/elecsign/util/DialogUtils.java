package com.feidi.elecsign.util;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.feidi.elecsign.R;

/**
 * 作者：wanglei on 2019/5/16.
 * 邮箱：forwlwork@gmail.com
 */
public class DialogUtils {

    public static void showAuthDescriptionDialog(Context context) {
        final Dialog dialog = new Dialog(context, R.style.AuthDescriptionDialogStyle);
        dialog.setContentView(R.layout.dialog_auth_description);
        dialog.findViewById(R.id.ibtn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public static void showCommonDialog(Context context, String content, final View.OnClickListener onSureClickListener) {
        final Dialog dialog = new Dialog(context, R.style.AuthDescriptionDialogStyle);
        dialog.setContentView(R.layout.dialog_common);
        ((TextView) dialog.findViewById(R.id.tv_dialog_content)).setText(content);
        dialog.findViewById(R.id.tv_dialog_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.tv_dialog_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (onSureClickListener != null) {
                    onSureClickListener.onClick(v);
                }
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

}
