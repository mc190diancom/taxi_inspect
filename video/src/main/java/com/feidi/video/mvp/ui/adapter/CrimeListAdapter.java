package com.feidi.video.mvp.ui.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.feidi.video.R;
import com.feidi.video.mvp.model.entity.CrimeInfo;
import com.feidi.video.mvp.ui.adapter.listener.OnItemContentViewClickListener;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.miu30.common.MiuBaseApp;
import com.miu30.common.ui.entity.AlarmInfo;
import com.miu30.common.util.BaiduMapGPSUtil;

import java.util.List;

/**
 * 作者：wanglei on 2019/6/3.
 * 邮箱：forwlwork@gmail.com
 */
public class CrimeListAdapter extends DefaultAdapter<AlarmInfo> {
    private OnItemContentViewClickListener<AlarmInfo> onLookClickListener;
    private Activity activity;

    public CrimeListAdapter(List<AlarmInfo> infos,Activity activity) {
        super(infos);
        this.activity = activity;
    }

    @Override
    public BaseHolder<AlarmInfo> getHolder(final View v, int viewType) {
        return new BaseHolder<AlarmInfo>(v) {
            @Override
            public void setData(final AlarmInfo data, final int position) {

                ((TextView) v.findViewById(R.id.item_tv_count))
                        .setText(data.getAlarmType());
                ((TextView) v.findViewById(R.id.item_tv_license)).setText(data.getVehiclePlatNo());
                ((TextView) v.findViewById(R.id.item_tv_distance))
                        .setText(BaiduMapGPSUtil.getDistance(data.getLatitude(),data.getLongitude())+"米");
                ImageView imageView = v.findViewById(R.id.item_iv_video);
                if(data.getPictureIDList() != null){
                    String imgAddr = data.getPictureIDList().get(1);
                    imgAddr = imgAddr.replace("10.212.160.152","10.252.16.83");
                    Glide.with(activity).load(imgAddr).into(imageView);
                }
                v.findViewById(R.id.item_tv_look).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onLookClickListener != null) {
                            onLookClickListener.onItemContentViewClick(v, data, position);
                        }
                    }
                });
                v.findViewById(R.id.item_iv_video).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onLookClickListener != null) {
                            onLookClickListener.onItemVideoViewClick(v, data, position);
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

    public void setOnLookClickListener(OnItemContentViewClickListener<AlarmInfo> onLookClickListener) {
        this.onLookClickListener = onLookClickListener;
    }
}
