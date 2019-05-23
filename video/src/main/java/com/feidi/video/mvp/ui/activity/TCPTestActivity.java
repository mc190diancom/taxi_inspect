package com.feidi.video.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.feidi.video.R;
import com.feidi.video.app.services.TCPConnectService;
import com.miu30.common.connect.entity.BindCameraRequest;
import com.miu30.common.connect.ChannelManager;
import com.miu30.common.connect.entity.LogoutRequest;
import com.miu30.common.connect.entity.NettyConstants;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

/**
 * 作者：wanglei on 2019/5/23.
 * 邮箱：forwlwork@gmail.com
 * <p>
 * 测试TCP长连接
 */
public class TCPTestActivity extends FragmentActivity {
    private TextView tvState;
    private TextView tvContent;
    private Button btnLogout;
    private Button btnBindCamera;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcp_test);

        tvState = findViewById(R.id.tv_state);
        tvContent = findViewById(R.id.tv_content);
        btnLogout = findViewById(R.id.btn_logout);
        btnBindCamera = findViewById(R.id.btn_bind_camera);

        if (ChannelManager.getInstance().isConnected()) {
            tvState.setText("当前状态：已连接");
        } else {
            tvState.setText("当前状态：未连接");
        }

        tvContent.setText("receive : ");

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChannelManager.getInstance().sendMessage(new LogoutRequest("zfzh1"));
            }
        });

        btnBindCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChannelManager.getInstance().sendMessage(new BindCameraRequest("zfzh1", "666666666,777777777"));
            }
        });

        startService(new Intent(this, TCPConnectService.class));
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopService(new Intent(this, TCPConnectService.class));
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscriber(tag = NettyConstants.TAG_TCP_MESSAGE)
    public void receiveMessage(String message) {
        tvContent.setText("receive : " + message);
    }

    @Subscriber(tag = NettyConstants.TAG_TCP_CONNECT_STATE)
    public void receiveState(boolean state) {
        if (state) {
            tvState.setText("当前状态：已连接");
        } else {
            tvState.setText("当前状态：未连接");
        }
    }

}
