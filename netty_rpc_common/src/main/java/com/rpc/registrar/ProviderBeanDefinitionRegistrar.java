package com.rpc.registrar;

import com.rpc.annotations.EnableRpcProvider;
import com.rpc.annotations.RpcProvider;
import com.rpc.netty.server.NettyServer;
import com.rpc.netty.server.NettyServerHandler;
import com.rpc.util.ClazzScanner;
import com.rpc.util.RpcBeanDefinitionRegistryUtil;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;
import java.util.Set;

/**
 * @Title：服务提供方bean注册器
 * @Author：wangchenggong
 * @Date 2021/3/26 16:39
 * @Description
 * @Version
 */
public class ProviderBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {


    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        /**
         * 1.【注册@RpcProvider的服务】
         * 找到指定包(@EnableRpcProvider)下 带有注解@RpcProvider的服务，注册 serveice的bean到Spring容器中
         */
        //扫描注解
        Map<String, Object> annotationAttributes = importingClassMetadata
                .getAnnotationAttributes(EnableRpcProvider.class.getName());
        String[] scanPackages = (String[])annotationAttributes.get("scanPackages");
        Set<Class<?>> rpcProviderClazzSet = ClazzScanner.scanClassWithAnnotation(scanPackages, RpcProvider.class);

        /**
         * 2.【注册netty Server】
         * netty Server实例化时会加载所有的@RpcProvider的服务实例到map中，
         * 并把map传递给netty Server Handler,
         * 以便netty Server Handler在处理客户端的请求时，可以利用策略模式+反射机制调用对应的服务实例进行业务处理
         */
        rpcProviderClazzSet.add(NettyServer.class);

        /**
         * 3.【注册netty Server Handler】
         */
        rpcProviderClazzSet.add(NettyServerHandler.class);

        RpcBeanDefinitionRegistryUtil.registerRpcBeanDefinition(rpcProviderClazzSet, registry);
    }




}
