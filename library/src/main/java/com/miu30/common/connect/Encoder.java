package com.miu30.common.connect;

import com.miu30.common.connect.entity.IMesage;
import com.miu30.common.util.GsonUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import timber.log.Timber;

/**
 * 作者：wanglei on 2019/5/22.
 * 邮箱：forwlwork@gmail.com
 */
public class Encoder extends MessageToByteEncoder<IMesage> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, IMesage mesage, ByteBuf byteBuf) throws Exception {
        if (mesage != null) {
            String sendMessage = GsonUtil.toJson(mesage);
            Timber.tag("netty").i("send -->> %s", sendMessage);
            byteBuf.writeBytes(sendMessage.getBytes());
        }
    }
}
