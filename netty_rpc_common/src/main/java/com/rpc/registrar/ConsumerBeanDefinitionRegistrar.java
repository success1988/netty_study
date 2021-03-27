package com.rpc.registrar;

import com.rpc.annotations.EnableRpcConsumer;
import com.rpc.netty.client.NettyClient;
import com.rpc.netty.client.NettyClientHandler;
import com.rpc.util.ClazzScanner;
import com.rpc.util.RpcBeanDefinitionRegistryUtil;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Title：服务消费方bean注册器
 * @Author：wangchenggong
 * @Date 2021/3/26 16:20
 * @Description
 * @Version
 */
public class ConsumerBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        //1.注册消费者代理工厂Bean： 可以自动装配到其他bean中，可以像操作其他serviceBean一样，方便使用
        Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(EnableRpcConsumer.class.getName());
        String[] scanPackages = (String[])annotationAttributes.get("scanPackages");
        Set<Class<?>> consumerClazzSet = ClazzScanner.scanInterfaces(scanPackages);
        RpcBeanDefinitionRegistryUtil.registerRpcBeanDefinition(consumerClazzSet, registry, ConsumerProxyFactoryBean.class);

        //2.注册方法拦截器:
        /**
         *         正常来说，nettyClient发送消息，NettyClientHandler异步读取消息， 但是
         *         这里做了【异步转同步】，所以可以认为是Netty发送消息方法是同步的，
         *         所以只需要让方法拦截器依赖nettyClient即可
         */
        Set<Class<?>> needBeans = new HashSet<>();



        //3.注册Netty Client: 用于创建网络连接； 用于发送网络请求，请求前会做序列化操作
        needBeans.add(NettyClient.class);

        //4.注册Netty Client Handler: 收到网络请求后的反序列化操作
        needBeans.add(NettyClientHandler.class);

        needBeans.add(ConsumerProxyMethodInterceptor.class);

        RpcBeanDefinitionRegistryUtil.registerRpcBeanDefinition(needBeans, registry);
    }
}
