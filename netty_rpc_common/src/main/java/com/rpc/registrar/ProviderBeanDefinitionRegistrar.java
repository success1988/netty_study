package com.rpc.registrar;

import com.rpc.annotations.EnableRpcProvider;
import com.rpc.netty.server.NettyServer;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;
import java.util.Map;

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
         * 【注册@RpcProvider的服务】
         * 找到指定包(@EnableRpcProvider)下 带有注解@RpcProvider的服务，注册 serveice的bean到Spring容器中
         */
        //扫描注解
        Map<String, Object> annotationAttributes = importingClassMetadata
                .getAnnotationAttributes(EnableRpcProvider.class.getName());
        String[] scanPackages = (String[])annotationAttributes.get("scanPackages");

        //扫描服务端组件（netty server相关类）
        ClassPathBeanDefinitionScanner beanScanner = new ClassPathBeanDefinitionScanner(registry);
        String[] serverPackages = Arrays.copyOf(scanPackages, scanPackages.length + 1);
        serverPackages[scanPackages.length] = NettyServer.class.getPackage().getName();
        beanScanner.scan(serverPackages);
    }

}
