package com.success.rpc.httpproxy;

import com.success.rpc.annotations.HttpRpcService;
import org.reflections.Reflections;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @Title：
 * @Author：wangchenggong
 * @Date 2021/3/23 10:13
 * @Description
 * @Version
 */
@Component
public class HttpProxyRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    /**
     * 该方法用来注册更多的bean到spring容器中
     * @param beanDefinitionRegistry
     * @throws BeansException
     */
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        //方式1： 通过自动包扫描来实现bean注册
        registerProxyBeanByClassPathScanner(beanDefinitionRegistry);

        //方式2： 扫描自定义注解，获取Class，然后包装为BeanDefinition, 最后注册到Spring中
        registerProxyBeanByCreateBeanDefinition(beanDefinitionRegistry);
    }


    /**
     *方式1： 通过自动包扫描来实现bean注册
     * @param beanDefinitionRegistry
     */
    private void registerProxyBeanByClassPathScanner(BeanDefinitionRegistry beanDefinitionRegistry){
        HttpProxyClassPathScanner httpProxyClassPathScanner = new HttpProxyClassPathScanner(beanDefinitionRegistry);
        httpProxyClassPathScanner.doScan("com.success.rpc.service");
    }

    /**
     * 方式2： 扫描自定义注解，获取Class，然后包装为BeanDefinition, 最后注册到Spring中
     * @param beanDefinitionRegistry
     */
    private void registerProxyBeanByCreateBeanDefinition(BeanDefinitionRegistry beanDefinitionRegistry) {
        Reflections reflections = new Reflections("com.success.rpc.service2");
        Set<Class<?>> rpcClazzSet = reflections.getTypesAnnotatedWith(HttpRpcService.class);

        //将 class包装为BeanDefinition ，注册到Spring的Ioc容器中
        HttpProxyBeanDefinitionRegistryUtil.registerHttpProxyBeanDefinition(rpcClazzSet, beanDefinitionRegistry);
    }


    /**
     * 该方法的实现中，主要用来对bean定义做一些改变
     * @param configurableListableBeanFactory
     * @throws BeansException
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }
}
