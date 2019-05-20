package com.feidi.elecsign.mvp.ui.adapter;

import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.feidi.elecsign.R;
import com.feidi.elecsign.mvp.model.entity.MyAuth;
import com.feidi.elecsign.mvp.ui.view.ThreeStateSwitch;
import com.feidi.elecsign.util.DialogUtils;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.miu30.common.MiuBaseApp;

import java.util.List;

/**
 * 作者：wanglei on 2019/5/17.
 * 邮箱：forwlwork@gmail.com
 */
public class MyAuthAdapter extends DefaultAdapter<MyAuth> {

    public MyAuthAdapter(List<MyAuth> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<MyAuth> getHolder(final View v, int viewType) {
        return new BaseHolder<MyAuth>(v) {
            @Override
            public void setData(MyAuth data, final int position) {
                TextView tvNameAndCard = v.findViewById(R.id.tv_name_and_card);
                TextView tvOverdue = v.findViewById(R.id.tv_overdue);
                final ThreeStateSwitch tssSwitch = v.findViewById(R.id.tss_switch);

                tvNameAndCard.setText(MiuBaseApp.self.getResources().getString(R.string.name_and_card_format
                        , data.getName()
                        , data.getCard()));
                tvOverdue.setText(data.getOverdue());
                tssSwitch.setState(data.getState());
                tssSwitch.setOnInterceptClickListener(new ThreeStateSwitch.OnInterceptClickListener() {
                    @Override
                    public void intercept() {
                        DialogUtils.showCommonDialog(v.getContext(), "确认继续向李毅授权签名一个月？", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ToastUtils.showShort("你点击了确认");
                                tssSwitch.setState(ThreeStateSwitch.STATE_ON);
                            }
                        });
                    }
                });
            }
        };
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_rv_my_auth;
    }
}
