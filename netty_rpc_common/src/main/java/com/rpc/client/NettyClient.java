package com.rpc.client;

import com.alibaba.fastjson.JSON;
import com.google.common.net.HostAndPort;
import com.rpc.client.connection.ConnectionManager;
import com.rpc.common.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.SynchronousQueue;

/**
 * @Title：netty网络通信客户端
 * @Author：wangchenggong
 * @Date 2021/3/26 16:52
 * @Description
 * @Version
 */
@Component
public class NettyClient{
    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);

    private EventLoopGroup group = new NioEventLoopGroup(1);
    private Bootstrap bootstrap = new Bootstrap();

    @Autowired
    private NettyClientHandler nettyClientHandler;
    @Autowired
    private ConnectionManager connectionManager;

    public NettyClient(){
            bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>(){
                    //创建NioSocketChannel成功后，在进行初始化时，将它的ChannelHandler设置到ChannelPipeline中，用于处理网络IO事件
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new IdleStateHandler(0, 0, 30));
                        pipeline.addLast(new JsonEncoder());
                        pipeline.addLast(new JsonDecoder());
                        pipeline.addLast("handler",nettyClientHandler);
                    }
                });
    }

    @PreDestroy
    public void destroy(){
        logger.info("RPC 客户端退出，释放资源");
        group.shutdownGracefully();
    }


    /**
     * 给服务端发送消息，并阻塞等待获取处理结果
     * @param request
     * @return
     * @throws InterruptedException
     */
    public Object send(RpcRequest request) throws InterruptedException{
        Channel channel = connectionManager.chooseChannel();
        if(channel != null && channel.isActive()){
            logger.info("向服务端{}发送消息:{}", channel.remoteAddress(), JSON.toJSONString(request));
            SynchronousQueue<Object> resultQueue = nettyClientHandler.sendMessage(request, channel);
            Object result = resultQueue.take();
            logger.info("接收到服务端{}响应结果:{}", channel.remoteAddress(), JSON.toJSONString(result));
            return result;
        }else{
            RpcResponse res = new RpcResponse();
            res.setCodeMsg(CodeMsgEnum.CHANNEL_EXCEPTION);
            return res;
        }
    }

    public Channel doConnect(SocketAddress socketAddress) throws InterruptedException {
        ChannelFuture future = bootstrap.connect(socketAddress);
        Channel channel = future.sync().channel();
        return channel;
    }

}
