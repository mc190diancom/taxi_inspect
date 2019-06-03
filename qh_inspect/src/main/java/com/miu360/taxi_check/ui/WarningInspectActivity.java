package com.miu360.taxi_check.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.feidi.video.mvp.ui.fragment.MoveCameraFragment;
import com.miu30.common.ui.view.TextSwitch;
import com.miu30.common.ui.widget.IncludeHeader;
import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.fragment.YujingDistributionFragment;

import java.util.ArrayList;

public class WarningInspectActivity extends BaseActivity {
    private ArrayList<Fragment> fragmentList;
    Fragment wcFragment;

    private MoveCameraFragment moveCameraFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect_warning);
        initView();
        InitViewPager();
    }

    /*
     * 初始化标签名
     */
    public void initView() {
        new IncludeHeader().init(self, "稽查预警");
        ((TextSwitch) findViewById(R.id.textSwitch)).setOnCheckedChangeListener(new TextSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(TextSwitch textSwitch, boolean isChecked) {
                if (isChecked) {
                    switchTab(0);
                } else {
                    switchTab(1);
                }
            }
        });
    }

    /*
     * 初始化ViewPager
     */
    public void InitViewPager() {
        fragmentList = new ArrayList<Fragment>();
        Fragment cdFragment = new YujingDistributionFragment();// 可疑车辆预警
//        wcFragment = new WarningCameraFragment();// 移动摄像头预警
        moveCameraFragment = MoveCameraFragment.newInstance();//移动摄像头预警

        fragmentList.add(cdFragment);
        fragmentList.add(moveCameraFragment);
        switchTab(0);
    }

    private Fragment mCurrentFragment;

    private void switchTab(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = fragmentList.get(position);
        if (mCurrentFragment == null) {
            transaction.add(R.id.warning_content, fragment, fragment.getClass().getName());
        } else {
            if (getSupportFragmentManager().findFragmentByTag(fragment.getClass().getName()) == null) {
                transaction.add(R.id.warning_content, fragment, fragment.getClass().getName());
                transaction.hide(mCurrentFragment);
            } else {
                transaction.hide(mCurrentFragment);
                transaction.show(fragment);
            }
        }
        mCurrentFragment = fragment;
        transaction.commitAllowingStateLoss();
    }
}
