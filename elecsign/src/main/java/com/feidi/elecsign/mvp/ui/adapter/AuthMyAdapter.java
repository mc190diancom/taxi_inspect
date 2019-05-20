package com.feidi.elecsign.mvp.ui.adapter;

import android.view.View;
import android.widget.TextView;

import com.feidi.elecsign.R;
import com.feidi.elecsign.mvp.model.entity.AuthMy;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.miu30.common.MiuBaseApp;

import java.util.List;

/**
 * 作者：wanglei on 2019/5/17.
 * 邮箱：forwlwork@gmail.com
 */
public class AuthMyAdapter extends DefaultAdapter<AuthMy> {
    public AuthMyAdapter(List<AuthMy> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<AuthMy> getHolder(final View v, int viewType) {
        return new BaseHolder<AuthMy>(v) {
            @Override
            public void setData(AuthMy data, int position) {
                TextView tvNameAndCard = v.findViewById(R.id.tv_name_and_card);
                TextView tvValidity = v.findViewById(R.id.tv_validity);

                tvNameAndCard.setText(MiuBaseApp.self.getString(R.string.name_and_card_format
                        , data.getName()
                        , data.getCard()));
                tvValidity.setText(data.getValidity());
            }
        };
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_rv_auth_my;
    }
}
