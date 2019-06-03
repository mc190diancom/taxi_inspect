package com.feidi.video.mvp.ui.adapter;

import android.view.View;
import android.widget.TextView;

import com.feidi.video.R;
import com.feidi.video.mvp.model.entity.CameraInfo;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;

import java.util.List;

/**
 * 作者：wanglei on 2019/5/30.
 * 邮箱：forwlwork@gmail.com
 */
public class CameraListAdapter extends DefaultAdapter<CameraInfo> {
    private OnItemClickListener<CameraInfo> onItemClickListener;

    public CameraListAdapter(List<CameraInfo> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<CameraInfo> getHolder(final View v, int viewType) {
        return new BaseHolder<CameraInfo>(v) {
            @Override
            public void setData(final CameraInfo data, final int position) {
                ((TextView) v.findViewById(R.id.item_tv_des)).setText(data.getName());

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(v, data, position);
                        }
                    }
                });
            }
        };
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_common;
    }

    public void setOnItemClickListener(OnItemClickListener<CameraInfo> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
