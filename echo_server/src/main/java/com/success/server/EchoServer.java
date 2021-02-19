package com.success.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * @Title：服务端引导程序
 * @Author：wangchenggong
 * @Date 2021/2/19 16:29
 * @Description
 * @Version
 */
public class EchoServer {

    private final int port;

    public EchoServer(int port){
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        //获取端口值
        int port = Integer.parseInt(args[0]);
        new EchoServer(port).start();
    }

    public void start() throws Exception{
        final EchoServerHandler serverHandler = new EchoServerHandler();
        //1.创建EventLoopGroup
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            //2.创建ServerBootstrap
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    //3.指定所使用的NIO传输Channel
                    .channel(NioServerSocketChannel.class)
                    //4.使用指定的端口设置套接字地址
                    .localAddress(new InetSocketAddress(port))
                    //5.添加一个EchoServerHandler到子Channel的ChannelPipeline
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(serverHandler);
                        }
                    });

            //6.异步绑定服务器：调用sync()方法阻塞等待 直到绑定完成
            ChannelFuture future = b.bind().sync();
            //7.获取channel的CloseFuture,并且阻塞当前线程，直到它完成
            future.channel().closeFuture().sync();
        }finally {
            //8.关闭EventLoopGroup，释放所有资源
            group.shutdownGracefully().sync();
        }

    }





}
