package com.feidi.video.mvp.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import org.videolan.vlc.MediaControl;
import org.videolan.vlc.VlcVideoView;

import butterknife.BindView;


public class VideoPlayActivity extends BaseMvpActivity<VideoPlayPresenter> implements VideoPlayContract.View {

    @BindView(R2.id.player)
    VlcVideoView vlcVideoView;
    @BindView(R2.id.tv_show)
    TextView tvShow;

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

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        IncludeHeader header = new IncludeHeader();
        header.init(self, "实时视频");
        String url = getIntent().getStringExtra("VAddress");
        vlcVideoView.setMediaListenerEvent(new MediaControl(vlcVideoView, tvShow));
        vlcVideoView.setPath(url);
        vlcVideoView.setCallBack(new VlcVideoView.startPlayListner() {
            @Override
            public void startPlay(int CurrentPosition) {
                System.out.println("videoSurfaceListener:getCurrentPosition===" + CurrentPosition);
                if (CurrentPosition > 0) {
                    Toast.makeText(self, "开始播放了", Toast.LENGTH_LONG).show();
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
    public void onPause() {
        super.onPause();
        vlcVideoView.pause();
    }

}
