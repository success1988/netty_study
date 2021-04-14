package com.success.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @Title：Nio客户端
 * @Author：wangchenggong
 * @Date 2021/4/14 11:36
 * @Description
 * @Version
 */
public class NioClient {

    public static void main(String[] args) throws IOException {

        SocketChannel socketChannel=SocketChannel.open(new InetSocketAddress("localhost", 9000));
        socketChannel.configureBlocking(false);


        ByteBuffer writeBuffer=ByteBuffer.wrap("HelloServer1".getBytes());
        socketChannel.write(writeBuffer);
        System.out.println("向服务端发送数据1结束");

        writeBuffer = ByteBuffer.wrap("HelloServer2".getBytes());
        socketChannel.write(writeBuffer);
        System.out.println("向服务端发送数据2结束");

        writeBuffer = ByteBuffer.wrap("HelloServer3".getBytes());
        socketChannel.write(writeBuffer);
        System.out.println("向服务端发送数据3结束");
    }


}
