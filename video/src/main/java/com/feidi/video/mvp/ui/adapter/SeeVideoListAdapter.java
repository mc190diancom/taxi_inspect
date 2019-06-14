package com.feidi.video.mvp.ui.adapter;

import android.view.View;
import android.widget.TextView;

import com.feidi.video.R;
import com.feidi.video.mvp.model.entity.CameraInfo;
import com.feidi.video.mvp.ui.adapter.listener.OnItemContentViewClickListener;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;

import java.util.List;

/**
 * 作者：wanglei on 2019/6/4.
 * 邮箱：forwlwork@gmail.com
 */
public class SeeVideoListAdapter extends DefaultAdapter<CameraInfo> {
    private OnItemContentViewClickListener<CameraInfo> onItemContentViewClickListener;

    public SeeVideoListAdapter(List<CameraInfo> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<CameraInfo> getHolder(final View v, int viewType) {
        return new BaseHolder<CameraInfo>(v) {
            @Override
            public void setData(final CameraInfo data, final int position) {
                ((TextView) v.findViewById(R.id.item_tv_camera_name)).setText(data.getNAME());
                ((TextView) v.findViewById(R.id.item_tv_camera_location)).setText(data.getDWMC());

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemContentViewClickListener != null) {
                            onItemContentViewClickListener.onItemContentViewClick(v, data, position);
                        }
                    }
                });
            }
        };
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_see_video;
    }

    public void setOnItemClickListener(OnItemContentViewClickListener<CameraInfo> listener) {
        this.onItemContentViewClickListener = listener;
    }
}
