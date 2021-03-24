package com.success.rpc.httpproxy;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @Title：
 * @Author：wangchenggong
 * @Date 2021/3/23 10:13
 * @Description
 * @Version
 */
@Component
public class HttpProxyRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        //通过包扫描来实现bean注册
        HttpProxyClassPathScanner httpProxyClassPathScanner = new HttpProxyClassPathScanner(beanDefinitionRegistry);

        httpProxyClassPathScanner.doScan("com.success.rpc.service");
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }
}
