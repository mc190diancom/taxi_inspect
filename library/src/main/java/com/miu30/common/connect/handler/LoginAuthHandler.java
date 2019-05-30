package com.miu30.common.connect.handler;

import com.miu30.common.connect.ChannelManager;
import com.miu30.common.connect.entity.IMesage;
import com.miu30.common.connect.entity.NettyConstants;
import com.miu30.common.connect.entity.LogoutRequest;
import com.miu360.library.BuildConfig;

import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import timber.log.Timber;

/**
 * 作者：wanglei on 2019/5/22.
 * 邮箱：forwlwork@gmail.com
 */
@ChannelHandler.Sharable
public class LoginAuthHandler extends ChannelInboundHandlerAdapter {
    //测试数据
    private String zfzh = "zfzh1";

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
        super.channelActive(channelHandlerContext);
        connectionOpen(channelHandlerContext);
        channelHandlerContext.writeAndFlush(new LoginRequest(zfzh, "123456"));
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        if (!(o instanceof String)) {
            return;
        }

        String message = (String) o;
        Timber.tag("netty").i("receive : %s", message);

        if (BuildConfig.LOG_DEBUG) {
            EventBus.getDefault().post(message, NettyConstants.TAG_TCP_MESSAGE);
        }

        JSONObject jsonObject = new JSONObject(message);
        if (jsonObject.optInt("msgType", -1) == IMesage.LOGIN) {
            if (jsonObject.optInt("status", -1) != 0) {
                //status不为0代表重复登录，所以此时先注销，再登录
                Timber.tag("netty").i("重复登录");
                //注销
                channelHandlerContext.writeAndFlush(new LogoutRequest(zfzh));
            } else {
                Timber.tag("netty").i("登录成功");
                channelHandlerContext.fireChannelRead(message);
            }
        } else {
            channelHandlerContext.fireChannelRead(message);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        ctx.fireExceptionCaught(cause);
        ctx.close();
        connectionClose();
    }

    private void connectionOpen(ChannelHandlerContext ctx) {
        Timber.tag("netty").i("连接打开");
        ChannelManager.getInstance().setChannel(ctx.channel());
        EventBus.getDefault().post(true, NettyConstants.TAG_TCP_CONNECT_STATE);
    }

    private void connectionClose() {
        Timber.tag("netty").i("连接关闭");
        ChannelManager.getInstance().removeChannel();
        EventBus.getDefault().post(false, NettyConstants.TAG_TCP_CONNECT_STATE);
    }

    private static class LoginRequest implements IMesage {
        //消息类型
        private int msgType = LOGIN;
        //执法账号
        private String zfzh;
        //密码
        private String pwd;

        LoginRequest(String zfzh, String pwd) {
            this.zfzh = zfzh;
            this.pwd = pwd;
        }
    }

}
