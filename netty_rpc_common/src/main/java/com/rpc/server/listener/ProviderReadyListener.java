package com.rpc.server.listener;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @Title：服务方启动就绪监听器
 * @Author：wangchenggong
 * @Date 2021/3/31 16:14
 * @Description
 * @Version
 */
@Component
public class ProviderReadyListener implements ApplicationListener<ApplicationReadyEvent> {


    private final Logger logger = LoggerFactory.getLogger(ProviderReadyListener.class);
    /**
     * Zk客户端
     */
    private CuratorFramework client = null;

    private TreeCache treeCache;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        //服务注册：将当前服务节点的ip写入到注册中心



        logger.error("》》》》》服务提供方已经就绪！");
    }
}
