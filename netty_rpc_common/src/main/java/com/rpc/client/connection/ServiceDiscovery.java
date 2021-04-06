package com.rpc.client.connection;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Title：服务发现
 * @Author：wangchenggong
 * @Date 2021/4/6 6:17
 * @Description
 * @Version
 */
@Component
public class ServiceDiscovery {
    private static final Logger logger = LoggerFactory.getLogger(ServiceDiscovery.class);
    private volatile Set<String> serviceAddressSet = new HashSet<>();
    /**
     * Zk客户端
     */
    private CuratorFramework client = null;
    private TreeCache treeCache;
    @Value("${rpc.registry.address}")
    private String registryHost;
    @Value("${rpc.registry.path}")
    private String registryPath;
    @Autowired
    private ConnectionManager connectionManager;
    /**
     *     初始化
     */
    @PostConstruct
    public void init() throws Exception {
        //与注册中心建立连接
        if(client==null) {
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
            client = CuratorFrameworkFactory.newClient(registryHost, retryPolicy);
            client.start();
            logger.info("consumer connected registry center! >>> {}", registryHost);
        }
        //监听子节点的变化
        doSubscribe();

        //获取子节点的数据，添加到list中,并更新连接管理器中的通道列表
        getAndUpdateServiceConnection();
    }

    /**
     * 订阅服务子节点的变化
     */
    private void doSubscribe(){
        try{
            if(client.checkExists().forPath(registryPath)==null) {
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(registryPath);
            }
            this.treeCache = new TreeCache(client, registryPath);
            treeCache.getListenable().addListener(new TreeCacheListener() {
                @Override
                public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent event) throws Exception {
                    TreeCacheEvent.Type type = event.getType();
                    if(type == TreeCacheEvent.Type.NODE_ADDED || type == TreeCacheEvent.Type.NODE_UPDATED
                        ||type == TreeCacheEvent.Type.NODE_REMOVED){
                        getAndUpdateServiceConnection();
                    }
                }
            });
            treeCache.start();
            logger.info("The consumer do subscribe  success!-----------------");
        }catch(Exception e){
            logger.error("The consumer do subscribe exception", e);
        }

    }

    /**
     * 获取服务节点列表，并更新连接管理器中的连接列表
     * @throws Exception
     */
    private void getAndUpdateServiceConnection() throws Exception {
        List<String> serviceNodes = client.getChildren().forPath(registryPath);
        serviceAddressSet.addAll(serviceNodes);

        connectionManager.updateConnectServer(serviceAddressSet);
    }


}
