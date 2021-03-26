package com.rpc.netty.common;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;



/**
 * json解码器
 * @author wangchenggong
 */
public class JsonDecoder extends LengthFieldBasedFrameDecoder {

    public JsonDecoder() {
        super(65535, 0, 4,0,4);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf decode = (ByteBuf) super.decode(ctx, in);
        if (decode==null){
            return null;
        }
        int data_len = decode.readableBytes();
        byte[] bytes = new byte[data_len];
        decode.readBytes(bytes);
        Object parse = JSON.parse(bytes);
        return parse;
    }
}
