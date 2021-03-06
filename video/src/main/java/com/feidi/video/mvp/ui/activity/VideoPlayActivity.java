package com.feidi.video.mvp.ui.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.feidi.video.R;
import com.feidi.video.R2;
import com.feidi.video.di.component.DaggerVideoPlayComponent;
import com.feidi.video.di.module.VideoPlayModule;
import com.feidi.video.mvp.contract.VideoPlayContract;
import com.feidi.video.mvp.presenter.VideoPlayPresenter;
import com.jess.arms.di.component.AppComponent;
import com.miu30.common.base.BaseMvpActivity;
import com.miu30.common.ui.widget.IncludeHeader;
import com.miu30.common.util.UIUtils;

import org.videolan.vlc.MediaControl;
import org.videolan.vlc.VlcVideoView;
import org.videolan.vlc.util.VLCInstance;

import butterknife.BindView;


public class VideoPlayActivity extends BaseMvpActivity<VideoPlayPresenter> implements VideoPlayContract.View, View.OnClickListener {

    @BindView(R2.id.player)
    VlcVideoView vlcVideoView;
    @BindView(R2.id.tv_show)
    TextView tvShow;
    @BindView(R2.id.btn_full)
    ImageView ivFull;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerVideoPlayComponent
                .builder()
                .appComponent(appComponent)
                .videoPlayModule(new VideoPlayModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_video_play;
    }

    RelativeLayout head;
    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        IncludeHeader header = new IncludeHeader();
        header.init(self, "实时视频");
        head = findViewById(R.id.header);
        String url = getIntent().getStringExtra("VAddress");
        ivFull.setOnClickListener(this);
        vlcVideoView.setMediaListenerEvent(new MediaControl(vlcVideoView, tvShow));
        vlcVideoView.setPath(url);
        vlcVideoView.setCallBack(new VlcVideoView.startPlayListner() {
            @Override
            public void startPlay(int CurrentPosition) {
                if (CurrentPosition > 0) {
                }
            }
        });

        startPlay();
    }


    private void startPlay() {
        vlcVideoView.startPlay();
    }

    @Override
    public void onResume() {
        super.onResume();
        vlcVideoView.start();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            head.setVisibility(View.GONE);
        }else{
            head.setVisibility(View.VISIBLE);
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onPause() {
        super.onPause();
        vlcVideoView.pause();
    }

    public boolean isFullscreen;
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btn_full){
            isFullscreen = !isFullscreen;
            if (isFullscreen) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            } else {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }else if(id == R.id.ibtn_left){
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        vlcVideoView.onDestory();
        VLCInstance.release();
        super.onDestroy();
    }
}
