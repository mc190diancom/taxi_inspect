package com.miu30.common.connect;

import com.miu30.common.connect.entity.NettyConstants;
import com.miu30.common.connect.handler.HeartBeatHandler;
import com.miu30.common.connect.handler.LoginAuthHandler;
import com.miu30.common.connect.handler.MessageHandler;
import com.miu30.common.util.Decoder;

import org.simple.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.netty.handler.codec.string.StringDecoder;
import timber.log.Timber;

/**
 * 作者：wanglei on 2019/5/22.
 * 邮箱：forwlwork@gmail.com
 */
public class NettyClient {
    private EventLoopGroup group = new NioEventLoopGroup();
    private boolean needReconnection = true;

    public void stop() {
        //不需要进行重连
        needReconnection = false;
        //关闭通道
        ChannelManager.getInstance().close();
        //关闭线程池
        group.shutdownGracefully();
        //通知连接已断开
        EventBus.getDefault().post(false, NettyConstants.TAG_TCP_CONNECT_STATE);
    }

    public void connect(String host, int port) {
        try {
            Timber.tag("netty").i("准备连接到 -->> host:%s ; port:%s", host, port);
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    //超时时间
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            //编码器
                            channel.pipeline().addLast("Encoder", new Encoder());
                            //json解码器
                            channel.pipeline().addLast(new JsonObjectDecoder());
                            //字符串解码器
                            channel.pipeline().addLast(new StringDecoder());
                            //登录认证处理器
                            channel.pipeline().addLast("LoginAuthHandler", new LoginAuthHandler());
                            //心跳处理器
                            channel.pipeline().addLast("HeartBeatHandler", new HeartBeatHandler());
                            //消息处理器
                            channel.pipeline().addLast(new MessageHandler());
                        }
                    });

            ChannelFuture future = bootstrap.connect(host, port).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //断线重连
            if (needReconnection) {
                try {
                    TimeUnit.SECONDS.sleep(15);
                    connect(host, port);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
