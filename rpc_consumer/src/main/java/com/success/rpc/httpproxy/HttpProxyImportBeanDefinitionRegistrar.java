package com.success.rpc.httpproxy;

import com.success.rpc.annotations.EnableHttpRpcConsumer;
import com.success.rpc.annotations.HttpRpcService;
import org.reflections.Reflections;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;
import java.util.Set;

/**
 * @Title：方式3：通过@Import注解动态导入bean
 * @Author：wangchenggong
 * @Date 2021/3/24 16:24
 * @Description
 * @Version
 */
public class HttpProxyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        //扫描注解
        Map<String, Object> annotationAttributes = importingClassMetadata
                .getAnnotationAttributes(EnableHttpRpcConsumer.class.getName());
        String scanPackage = (String)annotationAttributes.get("scanPackage");

        Reflections reflections = new Reflections(scanPackage);
        Set<Class<?>> rpcClazzSet = reflections.getTypesAnnotatedWith(HttpRpcService.class);
        //将 class包装为BeanDefinition ，注册到Spring的Ioc容器中
        HttpProxyBeanDefinitionRegistryUtil.registerHttpProxyBeanDefinition(rpcClazzSet, registry);
    }
}
