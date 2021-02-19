package com.success.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

/**
 * @Title：客户端的数据处理逻辑
 * @Author：wangchenggong
 * @Date 2021/2/19 17:14
 * @Description
 * @Version
 */
@ChannelHandler.Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //当被通知Channel是活跃的时候发送
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello ,Netty!", Charset.defaultCharset()));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        //记录已接收的消息
        System.out.println("client received: " +byteBuf.toString(Charset.defaultCharset()));

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //打印异常的堆栈信息
        cause.printStackTrace();
        //关闭该channel
        ctx.close();
    }
}
