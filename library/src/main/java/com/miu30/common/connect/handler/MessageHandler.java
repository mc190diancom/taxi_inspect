package com.miu30.common.connect.handler;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.miu30.common.MiuBaseApp;
import com.miu30.common.base.BaseData;
import com.miu30.common.connect.entity.IMesage;
import com.miu30.common.ui.entity.AlarmInfo;
import com.miu30.common.ui.entity.Template;
import com.miu30.common.util.UIUtils;

import org.json.JSONObject;

import java.util.List;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import timber.log.Timber;

/**
 * 作者：wanglei on 2019/5/22.
 * 邮箱：forwlwork@gmail.com
 */
@ChannelHandler.Sharable
public class MessageHandler extends ChannelInboundHandlerAdapter {

    private static final String FLAG = "com.feidi.cameraInfo";

    @SuppressLint("TimberArgCount")
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object o) throws Exception {
        super.channelRead(ctx, o);
        JSONObject jsonObject = new JSONObject((String) o);
        int type = jsonObject.optInt("msgType", -1);

        switch (type) {
            case IMesage.LOGOUT:
                Timber.tag("netty").i("登录注销");
                ctx.close();
                break;
            case IMesage.BIND_CAMERA:
                if (jsonObject.optInt("status", -1) == 0) {
                    UIUtils.toast(MiuBaseApp.self, "绑定摄像头成功", Toast.LENGTH_LONG);
                    Intent intent = new Intent();
                    intent.setAction(FLAG);
                    intent.putExtra("Data", "绑定成功");
                    LocalBroadcastManager.getInstance(MiuBaseApp.self).sendBroadcast(intent);
                } else {
                    UIUtils.toast(MiuBaseApp.self, "绑定摄像头失败", Toast.LENGTH_LONG);
                }
                break;
            case IMesage.ALARM:
                AlarmInfo alarmInfo = BaseData.gson.fromJson((String) o,new TypeToken<AlarmInfo>() {
                }.getType());
                UIUtils.toast(MiuBaseApp.self, "接收到报警信息:"+alarmInfo, Toast.LENGTH_LONG);
                break;
            case IMesage.CANCEL_BIND_CAMERA:
                if (jsonObject.optInt("status", -1) == 0) {
                    UIUtils.toast(MiuBaseApp.self, "取消绑定摄像头成功", Toast.LENGTH_LONG);
                } else {
                    UIUtils.toast(MiuBaseApp.self, "取消绑定摄像头失败", Toast.LENGTH_LONG);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
    }

}
