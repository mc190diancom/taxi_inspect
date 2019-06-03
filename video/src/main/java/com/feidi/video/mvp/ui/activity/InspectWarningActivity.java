package com.feidi.video.mvp.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ToggleButton;
import android.widget.ViewSwitcher;

import com.blankj.utilcode.util.ToastUtils;
import com.feidi.video.R;
import com.feidi.video.R2;
import com.feidi.video.di.component.DaggerInspectWarningComponent;
import com.feidi.video.mvp.contract.InspectWarningContract;
import com.feidi.video.mvp.model.entity.CameraInfo;
import com.feidi.video.mvp.model.entity.CrimeInfo;
import com.feidi.video.mvp.presenter.InspectWarningPresenter;
import com.feidi.video.mvp.ui.adapter.CameraListAdapter;
import com.feidi.video.mvp.ui.adapter.CrimeListAdapter;
import com.feidi.video.mvp.ui.adapter.IndustryOrWarningTypeAdapter;
import com.feidi.video.mvp.ui.adapter.OnItemClickListener;
import com.feidi.video.mvp.ui.adapter.OnItemContentViewClickListener;
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
    @BindView(R2.id.toggle)
    ToggleButton toggleBtn;

    private ViewStub vsCameraList;
    private ViewStub vsIndustryOrWarningType;
    private ViewStub vsCrime;

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
        vsCrime = findViewById(R.id.view_stub_crime);

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
        CameraListAdapter adapter = new CameraListAdapter(mPresenter.getCameraInfos());
        adapter.setOnItemClickListener(new OnItemClickListener<CameraInfo>() {
            @Override
            public void onItemClick(View v, final CameraInfo data, int position) {
                toggleBtn.toggle();

                if (vsCrime.getParent() != null) {
                    vsCrime.inflate();
                    final View rootView = findViewById(vsCrime.getInflatedId());
                    rootView.post(new Runnable() {
                        @Override
                        public void run() {
                            initCrimeView(rootView, data);
                        }
                    });
                } else {
                    mPresenter.updateCrimeList(data);
                }
            }
        });
        rvCamera.setAdapter(adapter);
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

    /** 犯案次数列表当前是否处于展开状态 */
    private boolean isExpend = false;

    /**
     * 初始化犯案次数列表所在的View
     *
     * @param rootView 根View
     */
    private void initCrimeView(View rootView, CameraInfo info) {
        final ImageView ivDirection = rootView.findViewById(R.id.iv_direction);
        final View dividerView = rootView.findViewById(R.id.divider);
        final LinearLayout llContainer = rootView.findViewById(R.id.ll_container);
        final int maxHeight = rootView.getHeight() - dividerView.getHeight();
        final int minHeight = llContainer.getHeight();

        ivDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimator(llContainer, ivDirection, dividerView, minHeight, maxHeight);
            }
        });

        initCrimeRecyclerView(rootView, info);
    }

    private void initCrimeRecyclerView(View rootView, CameraInfo info) {
        assert mPresenter != null;
        RecyclerView rvCrime = rootView.findViewById(R.id.rv_crime);
        rvCrime.addItemDecoration(mPresenter.getCrimeDecoration());
        rvCrime.setLayoutManager(new LinearLayoutManager(self));
        CrimeListAdapter adapter = mPresenter.getCrimeListAdapter(info);
        adapter.setOnLookClickListener(new OnItemContentViewClickListener<CrimeInfo>() {
            @Override
            public void onItemContentViewClick(View v, CrimeInfo data, int position) {
                ToastUtils.showShort("你点击了位置为" + position + "的查看按钮");
            }
        });
        rvCrime.setAdapter(adapter);
    }

    private void startAnimator(final LinearLayout llContainer, final ImageView ivDirection, final View dividerView, int minHeight, int maxHeight) {
        ValueAnimator animator;
        if (isExpend) {
            animator = ValueAnimator.ofInt(maxHeight, minHeight);
        } else {
            animator = ValueAnimator.ofInt(minHeight, maxHeight);
        }
        animator.setDuration(250);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isExpend = !isExpend;

                if (isExpend) {
                    dividerView.setVisibility(View.VISIBLE);
                    ivDirection.setBackgroundResource(R.drawable.ic_direction_down);
                } else {
                    dividerView.setVisibility(View.INVISIBLE);
                    ivDirection.setBackgroundResource(R.drawable.ic_direction_up);
                }
            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();
                ViewGroup.LayoutParams params = llContainer.getLayoutParams();
                params.height = value;
                llContainer.setLayoutParams(params);
            }
        });
        animator.start();
    }

}
