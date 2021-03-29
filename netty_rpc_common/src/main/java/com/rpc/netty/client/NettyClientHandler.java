package com.rpc.netty.client;

import com.alibaba.fastjson.JSON;
import com.rpc.netty.common.RpcRequest;
import com.rpc.netty.common.RpcResponse;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.SynchronousQueue;

/**
 * @Title：客户端处理器
 * @Author：wangchenggong
 * @Date 2021/3/27 9:26
 * @Description
 * @Version
 */
@Component
@ChannelHandler.Sharable
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);

    private ConcurrentSkipListMap<String, SynchronousQueue<Object>> queueMap = new ConcurrentSkipListMap<>();


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("已连接到RPC服务器.{}",ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("已与RPC服务器断开连接.{}",ctx.channel().remoteAddress());
        ctx.channel().close();
    }

    /**
     * 读取服务端的响应消息，即服务端对请求的处理结果
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcResponse rpcResponse = JSON.parseObject(msg.toString() ,RpcResponse.class);
        String requestId = rpcResponse.getRequestId();
        //向该请求id对应的阻塞队列中添加结果
        SynchronousQueue<Object> resultQueue = queueMap.get(requestId);
        resultQueue.put(rpcResponse);

        //然后从map中移除该请求id对应的键值对
        queueMap.remove(requestId);
    }

    /**
     * 发送消息
     * @param request
     * @param channel
     * @return
     */
    public SynchronousQueue<Object> sendMessage(RpcRequest request, Channel channel){
        // 为此次请求存一个专用的阻塞队列，用于接收异步响应结果
        SynchronousQueue<Object> resultQueue = new SynchronousQueue<>();
        queueMap.put(request.getId(), resultQueue);

        channel.writeAndFlush(request);
        return resultQueue;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.info("RPC通信服务器发生异常.{}", cause);
        ctx.channel().close();
    }
}
