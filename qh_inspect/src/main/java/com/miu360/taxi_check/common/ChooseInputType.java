package com.miu360.taxi_check.common;

import com.miu360.taxi_check.util.CNTextWatcher;
import com.miu360.taxi_check.util.CNTextWatcher.OnCNTextChangeListener;

import android.util.Log;
import android.widget.EditText;

public class ChooseInputType {
	

	public void init(EditText et) {
		et.addTextChangedListener(new CNTextWatcher(et, new OnCNTextChangeListener() {

			@Override
			public void onCnTextChange(String cn) {
				Log.e("", cn);
			}
		}));
	}

}
