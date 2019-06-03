package com.feidi.video.mvp.ui.adapter;

import android.view.View;
import android.widget.TextView;

import com.feidi.video.R;
import com.feidi.video.mvp.model.entity.CrimeInfo;
import com.feidi.video.mvp.ui.adapter.listener.OnItemContentViewClickListener;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.miu30.common.MiuBaseApp;

import java.util.List;

/**
 * 作者：wanglei on 2019/6/3.
 * 邮箱：forwlwork@gmail.com
 */
public class CrimeListAdapter extends DefaultAdapter<CrimeInfo> {
    private OnItemContentViewClickListener<CrimeInfo> onLookClickListener;

    public CrimeListAdapter(List<CrimeInfo> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<CrimeInfo> getHolder(final View v, int viewType) {
        return new BaseHolder<CrimeInfo>(v) {
            @Override
            public void setData(final CrimeInfo data, final int position) {
                ((TextView) v.findViewById(R.id.item_tv_count))
                        .setText(MiuBaseApp.self.getString(R.string.crime_count_format, data.getCount()));
                ((TextView) v.findViewById(R.id.item_tv_license)).setText(data.getCarLicense());
                ((TextView) v.findViewById(R.id.item_tv_distance))
                        .setText(MiuBaseApp.self.getString(R.string.crime_distance_format, data.getDistance()));

                v.findViewById(R.id.item_tv_look).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onLookClickListener != null) {
                            onLookClickListener.onItemContentViewClick(v, data, position);
                        }
                    }
                });
            }
        };
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_crime;
    }

    public void setOnLookClickListener(OnItemContentViewClickListener<CrimeInfo> onLookClickListener) {
        this.onLookClickListener = onLookClickListener;
    }
}
