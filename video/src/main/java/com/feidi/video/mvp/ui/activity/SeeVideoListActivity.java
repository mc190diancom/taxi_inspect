package com.feidi.video.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.feidi.video.R;
import com.feidi.video.R2;
import com.feidi.video.di.component.DaggerSeeVideoListComponent;
import com.feidi.video.mvp.contract.SeeVideoListContract;
import com.feidi.video.mvp.model.entity.CameraInfo;
import com.feidi.video.mvp.presenter.SeeVideoListPresenter;
import com.feidi.video.mvp.ui.adapter.SeeVideoListAdapter;
import com.feidi.video.mvp.ui.adapter.listener.OnItemContentViewClickListener;
import com.jess.arms.di.component.AppComponent;
import com.miu30.common.MiuBaseApp;
import com.miu30.common.base.BaseMvpActivity;
import com.miu30.common.data.UserPreference;
import com.miu30.common.ui.widget.IncludeHeader;
import com.miu30.common.util.UIUtils;

import java.util.ArrayList;

import butterknife.BindView;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/04/2019 16:11
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class SeeVideoListActivity extends BaseMvpActivity<SeeVideoListPresenter> implements SeeVideoListContract.View {
    @BindView(R2.id.rv_see_video)
    RecyclerView rvSeeVideo;
    private SeeVideoListAdapter adapter;
    public int SEEVIDEOCODE = 23;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSeeVideoListComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_see_video_list; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initHeader();
        initSeeVideoRecyclerView();
        assert mPresenter != null;
        mPresenter.getCameraInfos(new UserPreference(self).getString("user_name", ""));
    }

    private void initHeader() {
        IncludeHeader header = new IncludeHeader();
        header.init(self, "视频查看");
        header.setRightTextViewText("地图");
        header.setRightTextViewVisibility(View.VISIBLE);
        header.setRightTextViewOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(self, SeeVideoActivity.class);
                intent.putParcelableArrayListExtra("cameraInfo", (ArrayList<CameraInfo>) mPresenter.getCameraInfos());
                startActivityForResult(intent,SEEVIDEOCODE);
            }
        });
    }

    private void initSeeVideoRecyclerView() {
        assert mPresenter != null;
        rvSeeVideo.setLayoutManager(new LinearLayoutManager(self, LinearLayoutManager.VERTICAL, false));
        rvSeeVideo.addItemDecoration(mPresenter.getSeeCameraRecyclerViewDecoration());
        adapter = new SeeVideoListAdapter(mPresenter.getCameraInfos());
        adapter.setOnItemClickListener(new OnItemContentViewClickListener<CameraInfo>() {
            @Override
            public void onItemContentViewClick(View v, CameraInfo data, final int position) {
                mPresenter.getVideoAddress(data.getCAMERAID(),new UserPreference(self).getString("user_name",""));
            }

            @Override
            public void onItemVideoViewClick(View v, CameraInfo data, int position) {
                
            }
        });
        rvSeeVideo.setAdapter(adapter);
    }

    @Override
    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void getVideoAddressSuccess(String rtspUrl) {
        if(!TextUtils.isEmpty(rtspUrl)){
            if(rtspUrl.contains("10.212.160.158")){
                rtspUrl = rtspUrl.replace("10.212.160.158","10.252.16.81");
            }
            Intent intent = new Intent(self,VideoPlayActivity.class);
            intent.putExtra("VAddress",rtspUrl);
            startActivity(intent);
        }else{
            UIUtils.toast(self,"视频地址获取失败",Toast.LENGTH_SHORT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK && requestCode == SEEVIDEOCODE) {
            finish();
        }
    }
}
