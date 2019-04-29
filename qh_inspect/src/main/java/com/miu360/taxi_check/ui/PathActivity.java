package com.miu360.taxi_check.ui;

import com.miu360.inspect.R;
import com.miu360.inspect.R.layout;
import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.fragment.PathQueryFragment;
import com.miu360.taxi_check.view.HeaderHolder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

public class PathActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_path);

		HeaderHolder holder=new HeaderHolder();
		holder.init(self, "轨迹查询");
		holder.leftBtn.setVisibility(View.GONE);
		FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
//		transaction.show(new PathQueryFragment());

		Fragment fragment=new PathQueryFragment();
		transaction.add(R.id.fragment_container, fragment, fragment.getClass().getName());
		transaction.commit();

	}
}
