package com.miu30.common.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.miu30.common.MiuBaseApp;
import com.miu360.library.R;

public class CommonDialog {
	private Dialog dialog;
	private int timeCount;
	private OnCompleteListener onCompleteListener;

	public CommonDialog(Context ctx, int icon, String title, String okText, OnClickListener okListener,
                        String cancelText, final OnClickListener cancelListener, int timeCount,
                        OnCompleteListener onCompleteListener, boolean dismissOnOkClick) {
		dialog = getDialog(ctx, icon, title, okText, okListener, cancelText, cancelListener, dismissOnOkClick, false);
		makeNotFullScreen(dialog);
		this.timeCount = timeCount;
		this.onCompleteListener = onCompleteListener;
	}

	public CommonDialog(Context ctx, String title, String okText, OnClickListener okListener, String cancelText,
                        final OnClickListener cancelListener, int timeCount, OnCompleteListener onCompleteListener,
                        boolean dismissOnOkClick) {
		this(ctx, 0, title, okText, okListener, cancelText, cancelListener, timeCount, onCompleteListener,
				dismissOnOkClick, false);
	}

	public CommonDialog(Context ctx, String title, String okText, OnClickListener okListener, String cancelText,
                        final OnClickListener cancelListener, int timeCount, OnCompleteListener onCompleteListener,
                        boolean dismissOnOkClick, boolean sheet) {
		this(ctx, 0, title, okText, okListener, cancelText, cancelListener, timeCount, onCompleteListener,
				dismissOnOkClick, sheet);
	}

	public CommonDialog(Context ctx, int icon, String title, String okText, OnClickListener okListener,
                        String cancelText, final OnClickListener cancelListener, int timeCount,
                        OnCompleteListener onCompleteListener, boolean dismissOnOkClick, boolean sheet) {
		dialog = getDialog(ctx, icon, title, okText, okListener, cancelText, cancelListener, dismissOnOkClick, sheet);
		if (sheet) {
			Window window = dialog.getWindow();
			LayoutParams param = window.getAttributes();
			param.gravity = Gravity.BOTTOM;
			param.width = ((WindowManager) MiuBaseApp.self.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
					.getWidth();
			window.setAttributes(param);
		} else {
			makeNotFullScreen(dialog);
		}
		this.timeCount = timeCount;
		this.onCompleteListener = onCompleteListener;
	}

	public CommonDialog(Context ctx,boolean sheet) {
		dialog = getDialog(ctx,sheet);
		if (sheet) {
			Window window = dialog.getWindow();
			LayoutParams param = window.getAttributes();
			param.gravity = Gravity.CENTER;
			param.width = ((WindowManager) MiuBaseApp.self.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
					.getWidth();
			window.setAttributes(param);
		} else {
			makeNotFullScreen(dialog);
		}
		this.timeCount = timeCount;
		this.onCompleteListener = onCompleteListener;
	}

	public Dialog getDialog() {
		return dialog;
	}

	public void show() {
		dialog.show();
		if (timeCount > 0) {
			TextView cancelBtn = (TextView) dialog.findViewById(R.id.dialog_cancel);
			timeCounter(cancelBtn, cancelBtn.getText() + " ", "'", 10, onCompleteListener, dialog);
		}
	}

	public void dismiss() {
		dialog.dismiss();
	}

	public void setCustom(View v) {
		FrameLayout customLayout = (FrameLayout) dialog.findViewById(R.id.dialog_custom_layout);
		customLayout.removeAllViews();
		if (v != null) {
			customLayout.addView(v);
		}
	}

	public static Dialog getDialog(Context ctx, int icon, String title, String okText, final OnClickListener okListener,
			String cancelText, final OnClickListener cancelListener, final boolean dismissOnOkClick, boolean sheet) {
		int layout = R.layout.dialog_base;
		int style = R.style.clean_dialog;
		if (sheet) {
			layout = R.layout.dialog_base_sheet;
			style = R.style.sheet_dialog;
		}
		final Dialog dialog = new Dialog(ctx, style);
		dialog.setContentView(layout);

		TextView titleView = (TextView) dialog.findViewById(R.id.dialog_title);
		titleView.setText(title);
		if (icon > 0) {
			titleView.setCompoundDrawablesWithIntrinsicBounds(0, icon, 0, 0);
		}

		TextView okBtn = (TextView) dialog.findViewById(R.id.dialog_ok);
		if (okListener == null && TextUtils.isEmpty(okText)) {
			okBtn.setVisibility(View.GONE);
		} else {
			okBtn.setText(okText);
			if (okListener == null) {
				okBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
			} else {
				okBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (dismissOnOkClick) {
							dialog.dismiss();
						}
						okListener.onClick(v);
					}
				});

			}
		}
		TextView cancelBtn = (TextView) dialog.findViewById(R.id.dialog_cancel);
		if (cancelListener == null && TextUtils.isEmpty(cancelText)) {
			cancelBtn.setVisibility(View.GONE);
		}
		if (TextUtils.isEmpty(cancelText)) {
			cancelBtn.setVisibility(View.GONE);
		} else {
			cancelBtn.setText(cancelText);
			cancelBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
					if (cancelListener != null) {
						cancelListener.onClick(v);
					}
				}
			});
			dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					if (cancelListener != null) {
						cancelListener.onClick(null);
					}
				}
			});
		}

		// 没有取消事件则可以点击其它地方取消对话框
		dialog.setCanceledOnTouchOutside(cancelListener == null);

		if (!(ctx instanceof Activity)) {
			dialog.getWindow().setType(LayoutParams.TYPE_SYSTEM_ALERT);
		}

		return dialog;
	}

	public static Dialog getDialog(Context ctx, boolean sheet) {
		int layout = R.layout.layout_tip;
		int style = R.style.clean_dialog;
		final Dialog dialog = new Dialog(ctx, style);
		dialog.setContentView(layout);

		Button okBtn = dialog.findViewById(R.id.btn_save);
		okBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		return dialog;
	}

	public static void makeNotFullScreen(Dialog dialog) {
		Window window = dialog.getWindow();
		LayoutParams wmLayoutParams = window.getAttributes();
		// 获取屏幕宽、高用
		Display d = ((WindowManager) (dialog.getContext().getSystemService(Context.WINDOW_SERVICE)))
				.getDefaultDisplay();
		if (dialog.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			wmLayoutParams.width = (int) (d.getWidth() * 0.92); // 竖屏时，宽度设置为屏幕的0.92
		} else {
			wmLayoutParams.width = (int) (d.getWidth() * 0.6); // 横屏时，宽度设置为屏幕的0.6
		}
		// wmLayoutParams.gravity = Gravity.BOTTOM | Gravity.FILL_HORIZONTAL;
		window.setAttributes(wmLayoutParams);
	}

	public interface OnDialogItemClickListener {
		public void dialogItemClickListener(int position);
	}
	public static void timeCounter(final TextView tv, final String textLeft,
			final String textRight, final int timeout,
			final OnCompleteListener listener, final Object obj) {
		if (timeout >= 0) {
			Handler timer = new Handler(Looper.getMainLooper()) {
				@Override
				public void handleMessage(Message msg) {
					tv.setText(textLeft + msg.arg1 + textRight);
					if (msg.arg1 == 0) {
						removeMessages(0);
						if (listener != null) {
							listener.onComplete(obj);
						}
					} else {
						Message newMsg = obtainMessage(0, --msg.arg1, 0);
						sendMessageDelayed(newMsg, 1000);
					}
				}
			};
			int timeCount = timeout;
			Message msg = timer.obtainMessage(0, timeCount--, 0);
			timer.sendMessage(msg);
		}
	}

	public interface OnCompleteListener {
		public void onComplete(Object obj);
	}
}
