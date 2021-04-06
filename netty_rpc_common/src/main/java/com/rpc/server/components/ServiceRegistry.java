package com.rpc.server.components;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Title：服务提供方的服务注册
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
    @Value("${rpc.registry.address}")
    private String registryHost;
    @Value("${rpc.registry.path}")
    private String registryPath;

    @Override
    public void afterPropertiesSet() throws Exception {
        connectRegistryServer();
    }

    /**
     * 与注册中心建立连接
     */
    private void connectRegistryServer() {
        if(client==null) {
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
            client = CuratorFrameworkFactory.newClient(registryHost, retryPolicy);
            client.start();
            logger.info("service provider connected registry center! >>> {}", registryHost);
        }
    }

    /**
     * 将自己注册到注册中心，在服务提供方就绪后调用
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


}
