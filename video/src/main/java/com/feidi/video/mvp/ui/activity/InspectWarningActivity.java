package com.feidi.video.mvp.ui.activity;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.blankj.utilcode.util.SizeUtils;
import com.feidi.video.R;
import com.feidi.video.R2;
import com.feidi.video.di.component.DaggerInspectWarningComponent;
import com.feidi.video.mvp.contract.InspectWarningContract;
import com.feidi.video.mvp.presenter.InspectWarningPresenter;
import com.jess.arms.di.component.AppComponent;
import com.miu30.common.base.BaseMvpActivity;
import com.miu30.common.ui.view.TextSwitch;
import com.miu30.common.ui.widget.IncludeHeader;

import butterknife.BindView;


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
    public TextSwitch textSwitch;

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

        textSwitch.post(new Runnable() {
            @Override
            public void run() {
                // createPopupWindow();
            }
        });
    }

    private ValueAnimator animator;

    private void createPopupWindow() {
        final PopupWindow window = new PopupWindow();
        View view = getLayoutInflater().inflate(R.layout.layout_bottom_popoup_window, null, false);
        window.setContentView(view);

        window.getContentView().findViewById(R.id.tv_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("aaa", "cli");
                animator.start();
            }
        });

        window.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        window.setHeight(SizeUtils.dp2px(113));
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);

        animator = ValueAnimator.ofInt(SizeUtils.dp2px(113), SizeUtils.dp2px(442));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();
                Log.e("aaa", "update -- >>" + value);
                window.update(ViewGroup.LayoutParams.MATCH_PARENT, value);
            }
        });
        animator.setDuration(1000);

    }

}
