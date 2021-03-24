package com.success.rpc.httpproxy;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;

import java.util.Set;

/**
 * @Title：代理类bean定义的注册工具类
 * @Author：wangchenggong
 * @Date 2021/3/24 16:38
 * @Description
 * @Version
 */
public class HttpProxyBeanDefinitionRegistryUtil {


    /**
     * 将 class包装为BeanDefinition ，注册到Spring的Ioc容器中
     * @param proxyClazzSet
     * @param registry
     */
    public static void registerHttpProxyBeanDefinition(Set<Class<?>> proxyClazzSet, BeanDefinitionRegistry registry){

        for (Class<?> targetClazz : proxyClazzSet) {
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(targetClazz);
            GenericBeanDefinition definition = (GenericBeanDefinition) beanDefinitionBuilder.getRawBeanDefinition();
            //设置构造方法的参数  对于Class<?>,既可以设置为Class,也可以传Class的完全类名
            //definition.getConstructorArgumentValues().addGenericArgumentValue(targetClazz);
            definition.getConstructorArgumentValues().addGenericArgumentValue(targetClazz.getName());

            //Bean的类型，指定为某个代理接口的类型
            definition.setBeanClass(HttpProxyFactoryBean.class);
            //表示 根据代理接口的类型来自动装配
            definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
            registry.registerBeanDefinition(targetClazz.getName(),definition);
        }
    }

}
