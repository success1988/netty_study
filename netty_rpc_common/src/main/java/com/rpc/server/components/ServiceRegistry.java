package com.rpc.server.components;

import com.rpc.util.NetUtil;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Title：
 * @Author：wangchenggong
 * @Date 2021/4/5 11:25
 * @Description
 * @Version
 */
@Component
public class ServiceRegistry implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(ServiceRegistry.class);
    /**
     * Zk客户端
     */
    private CuratorFramework client = null;
    @Value("${rpc.server.address}")
    private String providerAddress;
    @Value("${rpc.registry.host}")
    private String registryHost;
    @Value("${rpc.registry.path}")
    private String registryPath;

    private TreeCache treeCache;

    private void connectRegistryServer() {
        //与注册中心建立连接
        if(client==null) {
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
            client = CuratorFrameworkFactory.newClient(registryHost, retryPolicy);
            client.start();
            logger.info("The CuratorFramework is connected! >>> {}", registryHost);
        }
    }

    /**
     * 将自己注册到注册中心
     */
    public void doRegisterSelf() throws Exception{
        String serviceNodePath = this.registryPath+"/"+providerAddress;
        Stat stat = client.checkExists().forPath(serviceNodePath);
        if(stat != null){
            client.delete().deletingChildrenIfNeeded().forPath(serviceNodePath);
        }
        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(serviceNodePath);
        client.setData().forPath(serviceNodePath,serviceNodePath.getBytes("UTF-8"));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        connectRegistryServer();
    }
}
