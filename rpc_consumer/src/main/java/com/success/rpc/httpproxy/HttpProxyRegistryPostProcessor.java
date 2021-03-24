package com.success.rpc.httpproxy;

import com.success.rpc.annotations.HttpRpcService;
import com.success.rpc.components.MyComponent;
import com.success.rpc.components.MyUser;
import com.success.rpc.components.impl.MyComponentB;
import org.reflections.Reflections;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @Title：bean的后置处理器，用于动态注册bean
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
        /**
         * 【注意】 以下代码与http rpc动态代理无关，只是用于说明BeanFactory也可以用于Spring中bean的动态注册
         *
         * configurableListableBeanFactory.registerResolvableDependency(MyComponent.class, new MyComponentB()); 方法，
         * 这行代码的意思是，当有其他类要注入 MyComponent类型的对象时，就给他注入我们这里自己创建的 MyComponentB 对象
         *
         * 解决的同一个接口有两个bean实例的问题（除了@Primary注解，这个方式也可以解决）
         *
         * Consider marking one of the beans as @Primary, updating the consumer to accept multiple beans,
         * or using @Qualifier to identify the bean that should be consumed
         * ————————————————
         * 版权声明：本文为CSDN博主「子♂衿」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
         * 原文链接：https://blog.csdn.net/yuge1123/article/details/106053857/
         */
        configurableListableBeanFactory.registerResolvableDependency(MyComponent.class, new MyComponentB());
        configurableListableBeanFactory.registerSingleton("user", new MyUser());
    }
}
