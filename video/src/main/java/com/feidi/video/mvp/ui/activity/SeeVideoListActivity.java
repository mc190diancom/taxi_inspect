package com.feidi.video.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

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
import com.miu30.common.base.BaseMvpActivity;
import com.miu30.common.ui.widget.IncludeHeader;

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
    }

    private void initHeader() {
        IncludeHeader header = new IncludeHeader();
        header.init(self, "视频查看");
        header.setRightTextViewText("地图");
        header.setRightTextViewVisibility(View.VISIBLE);
        header.setRightTextViewOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort("你点击了地图");
            }
        });
    }

    private void initSeeVideoRecyclerView() {
        assert mPresenter != null;
        rvSeeVideo.setLayoutManager(new LinearLayoutManager(self, LinearLayoutManager.VERTICAL, false));
        rvSeeVideo.addItemDecoration(mPresenter.getSeeCameraRecyclerViewDecoration());
        SeeVideoListAdapter adapter = new SeeVideoListAdapter(mPresenter.getCameraInfos());
        adapter.setOnItemClickListener(new OnItemContentViewClickListener<CameraInfo>() {
            @Override
            public void onItemContentViewClick(View v, CameraInfo data, final int position) {
                Intent intent = new Intent(self, SeeVideoActivity.class);
                intent.putExtra("cameraInfo", data);
                startActivity(intent);
            }
        });
        rvSeeVideo.setAdapter(adapter);
    }

}
