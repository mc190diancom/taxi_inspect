package com.miu360.legworkwrit.mvp.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.miu30.common.MiuBaseApp;
import com.miu30.common.base.BaseData;
import com.miu30.common.config.Config;
import com.miu360.legworkwrit.R;
import com.miu360.legworkwrit.mvp.data.CacheManager;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.mvp.model.entity.InquiryRecordPhoto;
import com.miu360.legworkwrit.mvp.model.entity.PhotoID;
import com.miu360.legworkwrit.mvp.ui.activity.PhotoActivity;

import java.util.List;

/**
 * 作者：wanglei on 2018/10/11.
 * 邮箱：forwlwork@gmail.com
 */
public abstract class UploadListAdapter extends DefaultAdapter<InquiryRecordPhoto> {
    private List<InquiryRecordPhoto> infos;
    private Activity activity;
    private boolean history;

    private Case getCase() {
        return CacheManager.getInstance().getCase();
    }

    public UploadListAdapter(boolean history,Activity activity, @NonNull List<InquiryRecordPhoto> infos) {
        super(infos);
        this.history = history;
        this.activity = activity;
        this.infos = infos;
    }

    @Override
    public BaseHolder<InquiryRecordPhoto> getHolder(final View v, int viewType) {
        return new BaseHolder<InquiryRecordPhoto>(v) {
            @Override
            public void setData(final InquiryRecordPhoto data, final int position) {
                TextView tvStartTime = v.findViewById(R.id.item_tv_start_time);
                TextView tvEndTime = v.findViewById(R.id.item_tv_end_time);

                tvStartTime.setText(data.getSTARTUTC());
                tvEndTime.setText(data.getENDUTC());

                ImageView ivPhoto = v.findViewById(R.id.item_iv_photo);
                final String intentUrl;
                if (getCase() == null) {
                    Glide.with(MiuBaseApp.self).load(data.getZPLJ()).into(ivPhoto);
                    intentUrl = data.getZPLJ();
                } else {
                    String url = Config.SERVER_BLLIST + "?type=dowmLoadXwblPhoto&jsonStr="
                            + BaseData.gson.toJson(new PhotoID(data.getID()));
                    intentUrl = url;
                    Glide.with(MiuBaseApp.self).load(url).into(ivPhoto);
                }
                if(history){
                    v.findViewById(R.id.item_btn_delete).setVisibility(View.GONE);
                    v.findViewById(R.id.item_cb_check).setVisibility(View.INVISIBLE);
                    v.findViewById(R.id.item_ibtn_edit_start_time).setVisibility(View.GONE);
                    v.findViewById(R.id.item_ibtn_edit_end_time).setVisibility(View.GONE);
                }else{
                    v.findViewById(R.id.item_btn_delete).setVisibility(View.VISIBLE);
                    v.findViewById(R.id.item_btn_delete).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            delete(data, infos.size());
                        }
                    });
                    v.findViewById(R.id.item_ibtn_edit_start_time).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            modifyStartTime(data);
                        }
                    });
                    v.findViewById(R.id.item_ibtn_edit_end_time).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            modifyEndTime(data);
                        }
                    });

                    ((CheckBox) v.findViewById(R.id.item_cb_check)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            data.setChecked(isChecked);
                        }
                    });
                }

                v.findViewById(R.id.item_iv_photo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, PhotoActivity.class);
                        intent.putExtra("url",intentUrl);
                        activity.startActivity(intent);
                    }
                });


            }
        };
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_rv_upload_list;
    }

    public abstract void delete(InquiryRecordPhoto photo, int itemCount);

    public abstract void modifyStartTime(InquiryRecordPhoto photo);

    public abstract void modifyEndTime(InquiryRecordPhoto photo);

}

