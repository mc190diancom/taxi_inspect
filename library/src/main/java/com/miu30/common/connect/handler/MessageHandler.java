package com.miu30.common.connect.handler;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
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
        Timber.tag("netty").i(jsonObject.toString());
        switch (type) {
            case IMesage.LOGOUT:
                Timber.tag("netty").i("登录注销");
                ctx.close();
                break;
            case IMesage.BIND_CAMERA:
                if (jsonObject.optInt("status", -1) == 0) {
                    UIUtils.toast(MiuBaseApp.self, "绑定摄像头成功", Toast.LENGTH_LONG);
                } else {
                    UIUtils.toast(MiuBaseApp.self, "绑定摄像头失败", Toast.LENGTH_LONG);
                }
                String cs = "{\"eventID\":\"00059cc85d79fd7d4676a7fcde3afbba831c\",\"alarmType\":\"套牌车辆\",\"msgType\":3,\"vehicleIndustryType\":\"巡游出租车\",\"latitude\":40.05179977416992,\"occurTime\":\"2019-06-14T16:36:14\",\"cameraIDList\":[\"11000000001325291355\"],\"pictureIDList\":[\"ftp://snap_ftp:snapftp12345678@10.212.160.152:12021/PicPath/2019-06-14/0004599ccdeed45144898be9227aec2e0c16.jpg\",\"ftp://snap_ftp:snapftp12345678@10.212.160.152:12021/PicPath/2019-06-14/0004f34a5f679ee24c969eaf3b180745c204.jpg\"],\"deviceID\":\"11000000001325291355\",\"deviceName\":\"T3B1出口\",\"vehiclePlatNo\":\"京BQ1117\",\"longitude\":116.6136016845703}";
                AlarmInfo alarmInfo2 = BaseData.gson.fromJson(cs,new TypeToken<AlarmInfo>() {
                }.getType());
                Intent intent2 = new Intent();
                intent2.setAction(FLAG);
                intent2.putExtra("data", alarmInfo2);
                MiuBaseApp.self.sendBroadcast(intent2);
                break;
            case IMesage.ALARM:
                AlarmInfo alarmInfo = BaseData.gson.fromJson(jsonObject.toString(),new TypeToken<AlarmInfo>() {
                }.getType());
                Intent intent = new Intent();
                intent.setAction(FLAG);
                intent.putExtra("data", alarmInfo);
                MiuBaseApp.self.sendBroadcast(intent);
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
