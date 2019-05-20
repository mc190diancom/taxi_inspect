package com.feidi.elecsign.util;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

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

}
