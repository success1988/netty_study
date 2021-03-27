package com.rpc.netty.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.net.HostAndPort;
import com.rpc.netty.common.JsonDecoder;
import com.rpc.netty.common.JsonEncoder;
import com.rpc.netty.common.RpcRequest;
import com.rpc.netty.common.RpcResponse;
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

import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.SynchronousQueue;

/**
 * @Title：
 * @Author：wangchenggong
 * @Date 2021/3/26 16:52
 * @Description
 * @Version
 */
public class NettyClient implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);

    private EventLoopGroup group = new NioEventLoopGroup(1);
    private Bootstrap bootstrap = new Bootstrap();
    private Channel channel;

    @Autowired
    private NettyClientHandler nettyClientHandler;
    @Value("${rpc.server.address}")
    private String serverAddress;

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

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            HostAndPort hostAndPort = HostAndPort.fromString(serverAddress);
            SocketAddress socketAddress = new InetSocketAddress(hostAndPort.getHostText(), hostAndPort.getPort());
            ChannelFuture future = bootstrap.connect(socketAddress);
            channel = future.sync().channel();
        } catch (InterruptedException e) {
            logger.error("与服务端{}建立连接发生异常",serverAddress, e);
        }
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
        if(channel != null && channel.isActive()){
            logger.info("向服务端{}发送消息:{}", channel.remoteAddress(), JSON.toJSONString(request));
            SynchronousQueue<Object> resultQueue = nettyClientHandler.sendMessage(request, channel);
            Object result = resultQueue.take();
            logger.info("接收到服务端{}响应结果:{}", channel.remoteAddress(), JSON.toJSONString(result));
            return result;
        }else{
            RpcResponse res = new RpcResponse();
            res.setCode(1);
            res.setErrorMsg("未正确连接到服务器.请检查相关配置信息!");
            return JSONArray.toJSONString(res);
        }
    }

}