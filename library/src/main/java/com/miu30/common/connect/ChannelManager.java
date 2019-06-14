package com.miu30.common.connect;

import com.miu30.common.connect.entity.IMesage;

import io.netty.channel.Channel;

/**
 * 作者：wanglei on 2019/5/22.
 * 邮箱：forwlwork@gmail.com
 */
public class ChannelManager {
    private Channel channel;

    private ChannelManager() {

    }

    private static class Holder {
        private static final ChannelManager INSTANCE = new ChannelManager();
    }

    public static ChannelManager getInstance() {
        return Holder.INSTANCE;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void removeChannel() {
        this.channel = null;
    }

    public void sendMessage(IMesage mesage) {
        if (isConnected()) {
            channel.writeAndFlush(mesage);
        }
    }

    public boolean isConnected() {
        return this.channel != null && this.channel.isOpen();
    }

    public void close() {
        if (this.channel != null) {
            this.channel.close();
        }
    }
}
