package com.rpc.util;

import com.rpc.registrar.ConsumerProxyFactoryBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.util.CollectionUtils;

import java.util.Set;

/**
 * @Title：RPC bean定义的注册工具类
 * @Author：wangchenggong
 * @Date 2021/3/24 16:38
 * @Description
 * @Version
 */
public class RpcBeanDefinitionRegistryUtil {




    public static void registerRpcBeanDefinition(Set<Class<?>> rpcClazzSet, BeanDefinitionRegistry registry){
        registerRpcBeanDefinition(rpcClazzSet, registry,null);
    }

    public static void registerRpcBeanDefinition(Set<Class<?>> rpcClazzSet, BeanDefinitionRegistry registry, Class<? extends FactoryBean> factoryBeanClazz){
        if(CollectionUtils.isEmpty(rpcClazzSet)){
            throw new RuntimeException("bean注册发生异常:没有可注册的Class");
        }

        for (Class<?> targetClazz : rpcClazzSet) {
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(targetClazz);
            GenericBeanDefinition definition = (GenericBeanDefinition) beanDefinitionBuilder.getRawBeanDefinition();
            if(factoryBeanClazz != null){
                //指定为对应的FactoryBean类型
                definition.setBeanClass(factoryBeanClazz);
                //为FactoryBean的构造方法入参赋值
                definition.getPropertyValues().add("rpcConsumerClazz", targetClazz);
            }else{
                //Bean的类型，指定为某个代理接口的类型
                definition.setBeanClass(targetClazz);
            }
            //表示 根据代理接口的类型来自动装配
            definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
            registry.registerBeanDefinition(targetClazz.getName(),definition);
        }

    }
}
