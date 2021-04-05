package com.rpc.registrar;

import com.rpc.annotations.EnableRpcConsumer;
import com.rpc.client.proxy.ConsumerProxyFactoryBean;
import com.rpc.client.NettyClient;
import com.rpc.util.ClazzScanner;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.CollectionUtils;

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
        //扫描客户端组件（netty client相关类），并注册到spring中
        ClassPathBeanDefinitionScanner beanScanner = new ClassPathBeanDefinitionScanner(registry);
        beanScanner.scan(NettyClient.class.getPackage().getName());

        //注册消费者代理工厂Bean： 可以自动装配到其他bean中，可以像操作其他serviceBean一样，方便使用
        Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(EnableRpcConsumer.class.getName());
        String[] scanPackages = (String[])annotationAttributes.get("scanPackages");
        Set<Class<?>> consumerClazzSet = ClazzScanner.scanInterfaces(scanPackages);

        if(CollectionUtils.isEmpty(consumerClazzSet)){
            throw new RuntimeException("bean注册发生异常:没有可注册的Class");
        }

        for (Class<?> targetClazz : consumerClazzSet) {
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(targetClazz);
            GenericBeanDefinition definition = (GenericBeanDefinition) beanDefinitionBuilder.getRawBeanDefinition();
            //指定为对应的FactoryBean类型
            definition.setBeanClass(ConsumerProxyFactoryBean.class);
            //为FactoryBean的构造方法入参赋值
            definition.getConstructorArgumentValues().addGenericArgumentValue(targetClazz);
            //表示 根据代理接口的类型来自动装配
            definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
            registry.registerBeanDefinition(targetClazz.getName(),definition);
        }
    }
}
