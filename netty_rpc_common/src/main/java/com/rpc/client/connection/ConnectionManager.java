package com.rpc.client.connection;

import com.google.common.net.HostAndPort;
import com.rpc.client.NettyClient;
import io.netty.channel.Channel;
import io.netty.util.internal.ThreadLocalRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @Title：
 * @Author：wangchenggong
 * @Date 2021/4/5 12:42
 * @Description
 * @Version
 */
@Component
public class ConnectionManager {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);
    @Autowired
    private NettyClient nettyClient;

    private Map<SocketAddress, Channel> channelMap = new ConcurrentHashMap<>();

    public Channel chooseChannel(){
        int size = channelMap.size();
        if(size>0){
            Collection<Channel> values = channelMap.values();
            //随机获取一个服务节点
            int index = ThreadLocalRandom.current().nextInt(size);
            return values.toArray(new Channel[0])[index];
        }
        return null;
    }

    public synchronized void updateConnectServer(List<String> addressList) throws InterruptedException {
        if(CollectionUtils.isEmpty(addressList)){
            closeAllServerChannels();
            throw new RuntimeException("没有可用的服务节点");
        }

        //转换为Set<SocketAddress>
        Set<SocketAddress> newSocketAddressList = addressList.stream().map(address -> {
            HostAndPort hostAndPort = HostAndPort.fromString(address);
            return new InetSocketAddress(hostAndPort.getHostText(), hostAndPort.getPort());
        }).collect(Collectors.toSet());


        for(SocketAddress newSocket:newSocketAddressList){
            Channel newChannel = channelMap.get(newSocket);
            if(newChannel == null || !newChannel.isOpen()){
                Channel channel = nettyClient.doConnect(newSocket);
                channelMap.put(newSocket, channel);
            }
        }

        //取差集,关闭已经过时的通道
        Set<SocketAddress> socketAddresses = channelMap.keySet();
        Set<SocketAddress> oldSocketAddresses = new HashSet<>(socketAddresses);
        oldSocketAddresses.removeAll(newSocketAddressList);

        for (SocketAddress oldSocket:oldSocketAddresses) {
            Channel oldChannel = channelMap.get(oldSocket);
            if(oldChannel != null){
                oldChannel.close();
            }
        }
    }

    private void closeAllServerChannels() {
        Collection<Channel> values = channelMap.values();
        for (Channel value:values) {
            value.close();
        }
        channelMap.clear();
    }


    public void removeChannel(Channel channel){
        logger.info("从连接管理器中移除失效Channel.{}",channel.remoteAddress());
        channelMap.remove(channel.remoteAddress());
    }
}
