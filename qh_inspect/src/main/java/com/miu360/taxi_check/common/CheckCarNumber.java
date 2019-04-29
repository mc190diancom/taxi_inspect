package com.miu360.taxi_check.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.TextView;

public class CheckCarNumber {
	/**
	 * 选择车牌号(AlertDialog.builder().setItems())
	 */
	public static void checkParNumber(Context self,final TextView tv, final CharSequence[] item,String type) {

		new AlertDialog.Builder(self).setItems(item, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				tv.setText(item[which]);
			}
		}).setTitle(type).show();
	}

}
