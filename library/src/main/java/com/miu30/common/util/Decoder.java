package com.miu30.common.util;

import java.nio.charset.Charset;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

@ChannelHandler.Sharable
public class Decoder extends MessageToMessageDecoder<ByteBuf> {
    private Charset charset;

    public Decoder() {
        this(Charset.defaultCharset());
    }

    private Decoder(Charset charset) {
        if (charset == null) {
            throw new NullPointerException("charset");
        } else {
            this.charset = charset;
        }
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        String s = byteBuf.toString(this.charset).concat("}");
        list.add(s);
    }
}
