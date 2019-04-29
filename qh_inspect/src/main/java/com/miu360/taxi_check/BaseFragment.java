package com.miu360.taxi_check;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {
	protected BaseFragment self;
	protected Activity act;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		act = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		self = this;
	}
	
	
}
