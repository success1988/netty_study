package com.success.io.nio_selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @Title：引入多路复用器后的NIO客户端
 * @Author：wangchenggong
 * @Date 2021/4/14 14:39
 * @Description
 * @Version
 */
public class NioSelectorClient {

    public static void main(String[] args) throws IOException {

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        Selector selector = Selector.open();
        //要先向多路复用器注册，然后才可以跟服务端进行连接
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        socketChannel.connect(new InetSocketAddress("localhost", 9000));

        while (true){
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isConnectable()){
                    SocketChannel sc = (SocketChannel) key.channel();
                    if (sc.finishConnect()){
                        System.out.println("服务器连接成功");

                        ByteBuffer writeBuffer=ByteBuffer.wrap("HelloServer".getBytes());
                        sc.write(writeBuffer);
                        System.out.println("向服务端发送数据结束");
                    }
                }
            }
        }

        /**
         * 服务器连接成功
         * 向服务端发送数据结束
         */

    }
}
