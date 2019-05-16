package com.feidi.elecsign.mvp.ui.widget;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.feidi.elecsign.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：wanglei on 2019/5/14.
 * 邮箱：forwlwork@gmail.com
 */
public class ElecsignHeader {
    @BindView(R2.id.ibtn_left)
    ImageButton ibtnLeft;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.tv_left)
    TextView tvLeft;
    @BindView(R2.id.tv_right)
    TextView tvRight;

    public ElecsignHeader() {

    }

    public void init(final Activity activity) {
        this.init(activity, "");
    }

    public void init(final Activity activity, String title) {
        ButterKnife.bind(this, activity);
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }

        ibtnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

    public void setLeftTextViewVisibility(int visibility) {
        if (tvLeft.getVisibility() != visibility) {
            tvLeft.setVisibility(visibility);
        }
    }

    public void setLeftTextViewText(CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            tvLeft.setText(text);
        }
    }

    public void setLeftTextViewOnClickListener(View.OnClickListener listener) {
        if (listener != null) {
            tvLeft.setOnClickListener(listener);
        }
    }

    public void setRightTextViewVisibility(int visibility) {
        if (tvRight.getVisibility() != visibility) {
            tvRight.setVisibility(visibility);
        }
    }

    public void setRightTextViewText(CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            tvRight.setText(text);
        }
    }

    public void setRightTextViewOnClickListener(View.OnClickListener listener) {
        if (listener != null) {
            tvRight.setOnClickListener(listener);
        }
    }

}
