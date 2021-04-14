package com.success.io.nio_selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @Title：引入多路复用器后的NIO服务端
 * @Author：wangchenggong
 * @Date 2021/4/14 13:57
 * @Description
 * SelectionKey.OP_ACCEPT —— 接收连接继续事件，表示服务器监听到了客户连接，服务器可以接收这个连接了
 * SelectionKey.OP_CONNECT —— 连接就绪事件，表示客户与服务器的连接已经建立成功
 * SelectionKey.OP_READ —— 读就绪事件，表示通道中已经有了可读的数据，可以执行读操作了（通道目前有数据，可以进行读操作了）
 * SelectionKey.OP_WRITE —— 写就绪事件，表示已经可以向通道写数据了（通道目前可以用于写操作）
 *
 * 1.当向通道中注册SelectionKey.OP_READ事件后，如果客户端有向缓存中write数据，下次轮询时，则会 isReadable()=true；
 *
 * 2.当向通道中注册SelectionKey.OP_WRITE事件后，这时你会发现当前轮询线程中isWritable()一直为true，如果不设置为其他事件
 * @Version
 */
public class NioSelectorServer {

    public static void main(String[] args) throws IOException {

        /**
         * 创建server端，并且向多路复用器注册，让多路复用器监听连接事件
         */
        //创建ServerSocketChannel
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(9000));
        //设置ServerSocketChannel为非阻塞
        serverSocket.configureBlocking(false);
        //打开selector处理channel,即创建epoll
        Selector selector = Selector.open();
        //把ServerSocketChannel注册到selector上，并且selector对客户端的accept连接操作感兴趣
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("NioSelectorServer服务启动成功");


        while(true){
            //阻塞等待需要处理的事件发生
            selector.select();

            //获取selector中注册的全部事件的SelectionKey实例
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            //遍历selectionKeys,对事件进行处理
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                //如果是OP_ACCEPT事件，则进行连接和事件注册
                if(key.isAcceptable()){
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                    //接受客户端的连接
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    //把SocketChannel注册到selector上，并且selector对客户端的read操作(即读取来自客户端的消息)感兴趣
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    System.out.println("客户端"+socketChannel.getRemoteAddress()+"连接成功!");

                }else if(key.isReadable()){
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(128);
                    int len = socketChannel.read(byteBuffer);
                    if(len > 0){
                        System.out.println("接收到客户端"+socketChannel.getRemoteAddress()+"发来的消息，消息内容为:"+new String(byteBuffer.array()));
                    }else if(len == -1){
                        System.out.println("客户端断开连接");
                        //关闭该客户端
                        socketChannel.close();
                    }
                }
                //从事件集合里删除本次处理的key，防止下次select重复处理
                iterator.remove();
            }

        }

        /**
         * NioSelectorServer服务启动成功
         * 客户端/127.0.0.1:57070连接成功!
         * 接收到客户端/127.0.0.1:57070发来的消息，消息内容为:HelloServer
         * 客户端/127.0.0.1:57121连接成功!
         * 接收到客户端/127.0.0.1:57121发来的消息，消息内容为:HelloServer
         */

    }
}
