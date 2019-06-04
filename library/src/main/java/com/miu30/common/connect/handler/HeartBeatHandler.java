package com.miu30.common.connect.handler;

import com.miu30.common.MiuBaseApp;
import com.miu30.common.config.Config;
import com.miu30.common.config.MsgConfig;
import com.miu30.common.connect.entity.IMesage;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.ScheduledFuture;
import timber.log.Timber;

/**
 * 作者：wanglei on 2019/5/22.
 * 邮箱：forwlwork@gmail.com
 */
@ChannelHandler.Sharable
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {
    private volatile ScheduledFuture<?> heartBeat;

    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object o) throws Exception {
        JSONObject jsonObject = new JSONObject((String) o);
        int type = jsonObject.optInt("msgType", -1);
        int status = jsonObject.optInt("status", -1);

        if (status == 99) {
            //终端未发登录请求，消息非法，关闭连接
            ctx.close();
            return;
        }

        switch (type) {
            case IMesage.LOGIN:
                Timber.tag("netty").i("开启心跳 , status = %s", status);
                //登录成功，开启心跳
                heartBeat = ctx.executor().scheduleAtFixedRate(new HeartBeatTask(ctx), 0, 10, TimeUnit.SECONDS);
                break;
            case IMesage.HEARTBEAT:
                Timber.tag("netty").i("接收到服务器的心跳 , status = %s", status);
                break;
            default:
                ctx.fireChannelRead(o);
                break;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        //连接异常时断开心跳
        if (heartBeat != null) {
            heartBeat.cancel(true);
            heartBeat = null;
            Timber.tag("netty").i("关闭心跳");
        }
        ctx.fireExceptionCaught(cause);
    }

    final static String zfzh = MiuBaseApp.user.getString("user_name", "0000000");
    private static class HeartBeatTask implements Runnable {
        private ChannelHandlerContext ctx;

        HeartBeatTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            if (ctx != null) {
                Timber.tag("netty").i("send heartbeat");
                ctx.writeAndFlush(new HeartBeat(zfzh, MsgConfig.lng, MsgConfig.lat));
            }
        }
    }

    private static class HeartBeat implements IMesage {
        //消息类型
        private int msgType = HEARTBEAT;
        //执法账号
        private String zfzh;
        //经度
        private double lon;
        //纬度
        private double lat;

        HeartBeat(String zfzh, double lon, double lat) {
            this.zfzh = zfzh;
            this.lon = lon;
            this.lat = lat;
        }
    }

}
