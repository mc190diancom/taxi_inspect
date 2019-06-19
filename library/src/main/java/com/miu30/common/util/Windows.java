package com.miu30.common.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.miu360.library.R;

public class Windows {

    public static AlertDialog confirmUpDialog(Context ctx, View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

        AlertDialog dialog = builder.create();
        // 用dialog添加自定义的view
        dialog.setView(view);
        dialog.setCancelable(false);
        return dialog;

    }

    public static CommonDialog confirm(Context ctx, String msg, OnClickListener okListener) {
        CommonDialog dialog = new CommonDialog(ctx, ctx.getString(R.string.dialog_title),
                ctx.getString(R.string.dialog_ok), okListener, ctx.getString(R.string.dialog_cancel), null, 0, null,
                true);
        TextView msgView = (TextView) LayoutInflater.from(ctx).inflate(R.layout.dialog_content_msg, null);
        dialog.setCustom(msgView);
        msgView.setText(msg);
        dialog.show();
        return dialog;
    }

    public static CommonDialog confirm(Context ctx, String msg, OnClickListener okListener, OnClickListener cancelListener) {
        CommonDialog dialog = new CommonDialog(ctx, ctx.getString(R.string.dialog_title),
                ctx.getString(R.string.dialog_ok), okListener, ctx.getString(R.string.dialog_cancel), cancelListener, 0, null,
                true);
        TextView msgView = (TextView) LayoutInflater.from(ctx).inflate(R.layout.dialog_content_msg, null);
        dialog.setCustom(msgView);
        msgView.setText(msg);
        dialog.show();
        return dialog;
    }

    public static CommonDialog tipDialog(Context ctx) {
        final CommonDialog dialog = new CommonDialog(ctx, true);
        dialog.show();
        return dialog;
    }

    public static CommonDialog singleChoice(Context ctx, String title, String[] items,
                                            final CommonDialog.OnDialogItemClickListener onDialogItemClickListener) {

        final CommonDialog dialog = new CommonDialog(ctx, title, null, null, null, null, 0, null, true);
        final ListView listView = (ListView) LayoutInflater.from(ctx).inflate(R.layout.single_choise_list, null);
        listView.setAdapter(new ArrayAdapter<String>(ctx, R.layout.dialog_item, items));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                if (onDialogItemClickListener != null) {
                    onDialogItemClickListener.dialogItemClickListener(position);
                }
            }
        });

        // if(items.length==1){
        // listView.setDividerHeight(0);
        // }

        dialog.setCustom(listView);
        dialog.show();
        return dialog;
    }

    public static CommonDialog singleChoice2(Context ctx, String title, String[] items,
                                             final CommonDialog.OnDialogItemClickListener onDialogItemClickListener) {

        final CommonDialog dialog = new CommonDialog(ctx, title, null, null, null, null, 0, null, true);
        final ListView listView = (ListView) LayoutInflater.from(ctx).inflate(R.layout.single_choise_list, null);
        listView.setAdapter(new ArrayAdapter<String>(ctx, R.layout.dialog_item, items));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                if (onDialogItemClickListener != null) {
                    onDialogItemClickListener.dialogItemClickListener(position);
                }
            }
        });
        dialog.getDialog().setCanceledOnTouchOutside(false);
        dialog.setCustom(listView);
        dialog.show();
        return dialog;
    }

    public static MyProgressDialog waiting(final Context context) {
        final MyProgressDialog commonDialog = new MyProgressDialog(context, R.layout.common_waiting,
                R.style.clean_dialog) {
            private ImageView imageView;

            public void initListener() {
                imageView = (ImageView) findViewById(R.id.waiting);
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.wait);
                animation.setInterpolator(new LinearInterpolator());
                imageView.startAnimation(animation);
            }

            @Override
            public void closeListener() {
                imageView.clearAnimation();
            }
        };

        commonDialog.setCancelable(true);
        commonDialog.show();

        /*
         * //当加载出现超时情况，则取消Dialog加载框 Thread t = new Thread(new Runnable() {
         *
         * @Override public void run() { try {
         * Thread.sleep(8000);//让他显示8秒后，取消ProgressDialog
         * if(commonDialog.isShowing()){//6秒后还在加载则弹出网络超时提示框，并屏蔽dialog显示
         * UIUtils.toast(context, "网络超时", Toast.LENGTH_SHORT);
         * commonDialog.dismiss(); } } catch (InterruptedException e) {
         * e.printStackTrace(); }
         *
         * } }); t.start();
         */
        return commonDialog;
    }

    /**
     * 创建底部显示的Dialog
     *
     * @param context 上下文
     * @return Dialog
     */
    public static Dialog createBottomDialog(Context context, @LayoutRes int layoutId) {
        Dialog dialog = new Dialog(context, R.style.BottomDialogStyle);
        dialog.setContentView(layoutId);
        Window window = dialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.gravity = Gravity.BOTTOM;
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(attributes);
        return dialog;
    }
}
