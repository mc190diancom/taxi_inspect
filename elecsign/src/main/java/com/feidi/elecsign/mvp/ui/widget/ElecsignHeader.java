package com.feidi.elecsign.mvp.ui.widget;

import android.app.Activity;
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

    public ElecsignHeader() {

    }

    public void init(final Activity activity, String title) {
        ButterKnife.bind(this, activity);
        tvTitle.setText(title);

        ibtnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

}
