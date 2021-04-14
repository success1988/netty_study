package com.success.io.aio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * @Title：Aio客户端
 * @Author：wangchenggong
 * @Date 2021/4/14 16:56
 * @Description
 * @Version
 */
public class AioClient {

    public static void main(String[] args) throws Exception {

        //创建Aio客户端
        AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 9000)).get();
        //发送消息
        socketChannel.write(ByteBuffer.wrap("hello AIO server ！".getBytes()));
        //接收消息
        ByteBuffer buffer = ByteBuffer.allocate(128);
        Integer len = socketChannel.read(buffer).get();
        if(len != -1){
            //客户端收到消息:hello Aio Client!
            System.out.println("客户端收到消息:"+new String(buffer.array(), 0, len));
        }
    }


}
