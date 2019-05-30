package com.feidi.elecsign.mvp.ui.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.feidi.elecsign.R;
import com.feidi.elecsign.R2;
import com.feidi.elecsign.di.component.DaggerMySignatureComponent;
import com.feidi.elecsign.mvp.contract.MySignatureContract;
import com.feidi.elecsign.mvp.presenter.MySignaturePresenter;
import com.jess.arms.di.component.AppComponent;
import com.miu30.common.base.BaseMvpActivity;
import com.miu30.common.ui.widget.IncludeHeader;

import butterknife.BindView;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 05/16/2019 14:30
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 * <p>
 * 我的授权
 */
public class MySignatureActivity extends BaseMvpActivity<MySignaturePresenter> implements MySignatureContract.View {
    //--------------------竖屏独有的控件--------------------
    LinearLayout llSignatureHint;
    //--------------------横屏独有的控件--------------------
    TextView tvRewrite;
    //-------------------横竖屏共有的控件-------------------
    @BindView(R2.id.tv_signature)
    TextView tvSignature;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerMySignatureComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_my_signature; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //TODO:是否拥有签名
        boolean hasSignature = false;
        showContentView(hasSignature);
    }

    private void showContentView(boolean hasSignature) {
        IncludeHeader header = new IncludeHeader();
        header.init(self);
        header.setLeftTextViewVisibility(View.VISIBLE);
        header.setLeftTextViewText("返回");
        header.setLeftTextViewOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            //竖屏
            header.setRightTextViewVisibility(View.GONE);
            llSignatureHint = findViewById(R.id.ll_signature_hint);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tvSignature.getLayoutParams();
            if (hasSignature) {
                llSignatureHint.setVisibility(View.GONE);
                params.topMargin = SizeUtils.dp2px(67);
                tvSignature.setLayoutParams(params);
            } else {
                llSignatureHint.setVisibility(View.VISIBLE);
                params.topMargin = SizeUtils.dp2px(20);
                tvSignature.setLayoutParams(params);
            }
        } else {
            //横屏
            header.setRightTextViewText("保存签名");
            header.setRightTextViewVisibility(View.VISIBLE);
            header.setRightTextViewOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // save signature ...
                }
            });

            tvRewrite = findViewById(R.id.tv_rewrite);
            tvRewrite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // rewrite signature ...
                }
            });
        }
    }


}
