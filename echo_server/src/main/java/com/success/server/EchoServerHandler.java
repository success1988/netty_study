package com.success.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;


/**
 * @Title：服务端的数据处理逻辑
 * @Author：wangchenggong
 * @Date 2021/2/19 15:19
 * @Description
 * @Version
 */
@ChannelHandler.Sharable //标识一个ChannelHandler可以被多个Channel安全地共享
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        System.out.println("Server received:" + in.toString(Charset.forName("utf-8")));
        //将接收到的消息写给发送者，而不冲刷出站消息
        ctx.write(in);
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将未决消息冲刷到远程节点，并且关闭该channel  ？？？
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //打印异常的堆栈信息
        cause.printStackTrace();
        //关闭该channel
        ctx.close();
    }
}
