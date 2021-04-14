package com.success.io.aio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @Title：Aio服务端
 * @Author：wangchenggong
 * @Date 2021/4/14 17:05
 * @Description
 * @Version
 */
public class AioServer {

    public static void main(String[] args) throws Exception {
        final AsynchronousServerSocketChannel serverChannel = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(9000));
        serverChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
            @Override
            public void completed(AsynchronousSocketChannel socketChannel, Object attachment) {
                try{
                    System.out.println("2--"+Thread.currentThread().getName());
                    //接收客户端连接
                    serverChannel.accept(attachment,this);
                    System.out.println("客户端"+socketChannel.getRemoteAddress()+"已连接");

                    ByteBuffer buffer = ByteBuffer.allocate(128);
                    socketChannel.read(buffer, null, new CompletionHandler<Integer, Object>() {
                        @Override
                        public void completed(Integer result, Object attachment) {
                            System.out.println("3--"+Thread.currentThread().getName());
                            //flip方法将Buffer从写模式切换到读模式
                            //如果没有，就是从文件最后开始读取的，当然读出来的都是byte=0时候的字符。通过buffer.flip();这个语句，就能把buffer的当前位置更改为buffer缓冲区的第一个位置
                            buffer.flip();
                            System.out.println(new String(buffer.array(), 0, result));
                            socketChannel.write(ByteBuffer.wrap("hello Aio Client!".getBytes()));
                        }

                        @Override
                        public void failed(Throwable exc, Object attachment) {
                            exc.printStackTrace();
                        }
                    });

                }catch(Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable exc, Object attachment) {

            }
        });

        System.out.println("1‐‐main"+Thread.currentThread().getName());
        Thread.sleep(Integer.MAX_VALUE);
    }
    /**
     * 1‐‐mainmain
     * 2--Thread-9
     * 客户端/127.0.0.1:54821已连接
     * 3--Thread-8
     * hello AIO server ！
     * 2--Thread-9
     * 客户端/127.0.0.1:54942已连接
     * 3--Thread-7
     * hello AIO server ！
     */

}
