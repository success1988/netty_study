package com.success.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Title：Nio服务端
 * @Author：wangchenggong
 * @Date 2021/4/14 11:04
 * @Description
 * @Version
 */
public class NioServer {

    /**
     * 保存客户端连接
     */
    static List<SocketChannel> channelList = new ArrayList<>();


    public static void main(String[] args) throws IOException {
        //创建Nio ServerSocketChannel
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(9000));
        //设置ServerSocketChannel为非阻塞
        serverSocket.configureBlocking(false);
        System.out.println("Nio服务启动成功");

        while(true){
            //非阻塞模式accept方法不会阻塞
            /// NIO的非阻塞是由操作系统内部实现的，底层调用了linux内核的accept函数
            SocketChannel socketChannel = serverSocket.accept();
            if(socketChannel != null){
                System.out.println("连接成功");
                socketChannel.configureBlocking(false);
                channelList.add(socketChannel);
            }

            Iterator<SocketChannel> iterator = channelList.iterator();
            while(iterator.hasNext()){
                SocketChannel sc = iterator.next();
                ByteBuffer byteBuffer = ByteBuffer.allocate(128);
                //非阻塞模式read方法不会阻塞
                int len = sc.read(byteBuffer);

                if(len > 0){
                    System.out.println("接收到消息:" + new String(byteBuffer.array()));
                }else if(len == -1){
                    iterator.remove();
                    System.out.println("客户端断开连接");
                }
            }

        }



    }
}
