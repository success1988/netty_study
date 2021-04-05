package com.rpc.server.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private ServiceRegistry serviceRegistry;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {

        try {
            serviceRegistry.doRegisterSelf();
            logger.info("》》》》》服务提供方已经就绪！");
        } catch (Exception e) {
           logger.error("服务注册发生异常",e);
        }

    }
}
