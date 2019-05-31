package com.feidi.video.mvp.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ViewSwitcher;

import com.feidi.video.R;
import com.feidi.video.R2;
import com.feidi.video.di.component.DaggerInspectWarningComponent;
import com.feidi.video.mvp.contract.InspectWarningContract;
import com.feidi.video.mvp.presenter.InspectWarningPresenter;
import com.feidi.video.mvp.ui.adapter.CameraListAdapter;
import com.feidi.video.mvp.ui.adapter.IndustryOrWarningTypeAdapter;
import com.jess.arms.di.component.AppComponent;
import com.miu30.common.base.BaseMvpActivity;
import com.miu30.common.ui.view.TextSwitch;
import com.miu30.common.ui.widget.IncludeHeader;

import butterknife.BindView;
import butterknife.OnCheckedChanged;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 05/22/2019 09:45
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class InspectWarningActivity extends BaseMvpActivity<InspectWarningPresenter> implements InspectWarningContract.View {
    @BindView(R2.id.textSwitch)
    TextSwitch textSwitch;
    @BindView(R2.id.view_switcher)
    ViewSwitcher viewSwitcher;
    @BindView(R2.id.cb_industry)
    CheckBox cbIndustry;
    @BindView(R2.id.cb_warning_type)
    CheckBox cbWarningType;

    private ViewStub vsCameraList;
    private ViewStub vsIndustryOrWarningType;

    private IndustryOrWarningTypeAdapter industryOrWarningTypeAdapter;

    @Override

    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerInspectWarningComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_inspect_warning_2; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        new IncludeHeader().init(self, "稽查预警");
        vsCameraList = findViewById(R.id.view_stub_camera);
        vsIndustryOrWarningType = findViewById(R.id.view_stub_industry_or_warningtype);

        cbIndustry.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkedInner(buttonView, isChecked, cbWarningType.isChecked());
            }
        });

        cbWarningType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkedInner(buttonView, cbIndustry.isChecked(), isChecked);
            }
        });
    }

    @OnCheckedChanged(R2.id.toggle)
    public void toggle(boolean isChecked) {
        if (isChecked) {
            if (viewSwitcher.getDisplayedChild() != 0) {
                viewSwitcher.showPrevious();
            }
        } else {
            if (viewSwitcher.getDisplayedChild() != 1) {
                if (vsCameraList.getParent() != null) {
                    vsCameraList.inflate();
                    initCameraRecyclerView();
                }

                viewSwitcher.showNext();
            }
        }
    }

    private void initCameraRecyclerView() {
        assert mPresenter != null;

        RecyclerView rvCamera = findViewById(vsCameraList.getInflatedId())
                .findViewById(R.id.rv_camera);
        rvCamera.addItemDecoration(mPresenter.getCameraListDecoration());
        rvCamera.setLayoutManager(new LinearLayoutManager(self));
        rvCamera.setAdapter(new CameraListAdapter(mPresenter.getCameraInfos()));
    }

    private void checkedInner(CompoundButton button, boolean isIndustryChecked, boolean isWarningTypeChecked) {
        assert mPresenter != null;

        if (isIndustryChecked || isWarningTypeChecked) {
            if (vsIndustryOrWarningType.getParent() != null) {
                vsIndustryOrWarningType.inflate();
                initIndustryOrWarningTypeRecyclerView();
            }

            if (!isIndustryChecked) {
                //仅选中预警类别
                mPresenter.setIndustryOrWarningTypeInfos(false);
            } else if (!isWarningTypeChecked) {
                //仅选中行业
                mPresenter.setIndustryOrWarningTypeInfos(true);
            } else {
                //行业、预警类别均选中
                if (button == cbIndustry) {
                    mPresenter.setIndustryOrWarningTypeInfos(true);
                } else {
                    mPresenter.setIndustryOrWarningTypeInfos(false);
                }
            }

            industryOrWarningTypeAdapter.notifyDataSetChanged();
            //显示列表界面
            findViewById(vsIndustryOrWarningType.getInflatedId()).setVisibility(View.VISIBLE);
        } else {
            //行业和预警类别都未选中，则隐藏列表界面
            findViewById(vsIndustryOrWarningType.getInflatedId()).setVisibility(View.GONE);
        }

    }

    private void initIndustryOrWarningTypeRecyclerView() {
        assert mPresenter != null;
        RecyclerView rvIndustryOrWarningType = findViewById(vsIndustryOrWarningType.getInflatedId())
                .findViewById(R.id.rv_industry_and_warningtype);
        rvIndustryOrWarningType.addItemDecoration(mPresenter.getIndustryOrWarningTypeListDecoration());
        rvIndustryOrWarningType.setLayoutManager(new LinearLayoutManager(self));
        industryOrWarningTypeAdapter = new IndustryOrWarningTypeAdapter(mPresenter.getIndustryOrWarningTypeInfos());
        rvIndustryOrWarningType.setAdapter(industryOrWarningTypeAdapter);
    }

//    private ValueAnimator animator;
//
//    private void createPopupWindow() {
//        final PopupWindow window = new PopupWindow();
//        View view = getLayoutInflater().inflate(R.layout.layout_bottom_popoup_window, null, false);
//        window.setContentView(view);
//
//        window.getContentView().findViewById(R.id.tv_test).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e("aaa", "cli");
//                animator.start();
//            }
//        });
//
//        window.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
//        window.setHeight(SizeUtils.dp2px(113));
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        window.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
//
//        animator = ValueAnimator.ofInt(SizeUtils.dp2px(113), SizeUtils.dp2px(442));
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                int value = (Integer) animation.getAnimatedValue();
//                window.update(ViewGroup.LayoutParams.MATCH_PARENT, value);
//            }
//        });
//        animator.setDuration(1000);

//    }

}
