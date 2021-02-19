package com.success.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @Title：客户端引导程序
 * @Author：wangchenggong
 * @Date 2021/2/19 17:27
 * @Description
 * @Version
 */
public class EchoClient {

    private final String host;
    private final int port;


    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }


    public void start() throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new EchoClientHandler());
                        }
                    });

            ChannelFuture f = b.connect().sync();
            f.channel().closeFuture().sync();

        }finally {
            group.shutdownGracefully().sync();

        }

    }

    public static void main(String[] args) throws Exception {

        String host = args[0];
        int port = Integer.parseInt(args[1]);
        new EchoClient(host, port).start();


    }
}
