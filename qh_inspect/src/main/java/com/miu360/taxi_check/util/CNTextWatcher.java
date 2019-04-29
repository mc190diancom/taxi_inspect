package com.miu360.taxi_check.util;

import java.util.regex.Pattern;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class CNTextWatcher implements TextWatcher {
	private EditText et;
	private OnCNTextChangeListener listener;

	public CNTextWatcher(EditText et, OnCNTextChangeListener listener) {
		super();
		this.et = et;
		this.listener = listener;
	}

	public void setEt(EditText et) {
		this.et = et;
	}

	public void setListener(OnCNTextChangeListener listener) {
		this.listener = listener;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		String ss = s.toString();
		if (!ss.equals(trimCN(ss))) {
			et.removeTextChangedListener(this);
			et.setText(trimCN(ss));
			et.addTextChangedListener(this);
			et.setSelection(trimCN(ss).length());
		} else {
			if (listener != null) {
				listener.onCnTextChange(ss);
			}
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

	private String trimCN(String str) {
		String returnString = "";
		int len = str.length();

		for (int i = 0; i < len; i++) {
			String s = str.substring(i, i + 1);
			if (Pattern.compile("[\u4e00-\u9fa5]").matcher(s).matches()) {// ����
				returnString = returnString + s;
			}
		}

		return returnString;
	}

	public static interface OnCNTextChangeListener {
		public void onCnTextChange(String cn);
	}
}
