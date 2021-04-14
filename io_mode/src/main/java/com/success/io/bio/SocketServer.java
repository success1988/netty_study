package com.success.io.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Title：BIO的服务端
 * @Author：wangchenggong
 * @Date 2021/4/13 9:41
 * @Description
 * @Version
 */
public class SocketServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9000);
        while (true){
            System.out.println("等待连接...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("客户端"+clientSocket.getRemoteSocketAddress()+"连接了！");

            handle(clientSocket);
        }

    }

    private static void handle(Socket clientSocket)  throws IOException{
        byte[] bytes = new byte[1024];
        int read = clientSocket.getInputStream().read(bytes);
        System.out.println("read 客户端"+clientSocket.getRemoteSocketAddress()+"数据完毕");
        if(read != -1){
            System.out.println("接收到客户端的数据：" + new String(bytes, 0, read));
        }
        clientSocket.getOutputStream().write("HelloClient".getBytes());
        clientSocket.getOutputStream().flush();
    }

}
