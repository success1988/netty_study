package com.success.rpc.httpproxy;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

import java.util.Arrays;
import java.util.Set;

/**
 * @Title：
 * @Author：wangchenggong
 * @Date 2021/3/23 10:28
 * @Description
 * @Version
 */
public class HttpProxyClassPathScanner extends ClassPathBeanDefinitionScanner {

    public HttpProxyClassPathScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

        if (beanDefinitions.isEmpty()) {
            logger.warn("No RPC mapper was found in '"
                    + Arrays.toString(basePackages)
                    + "' package. Please check your configuration.");
        } else {
            GenericBeanDefinition definition;
            //创建动态代理对象bean，并动态注入到spring容器中
            for (BeanDefinitionHolder holder : beanDefinitions) {

                definition = (GenericBeanDefinition) holder.getBeanDefinition();

                /**
                 *  在这里，我们可以给该对象的属性注入对应的实例。比如mybatis，就在这里注入了dataSource和sqlSessionFactory，
                 *    注意，如果采用definition.getPropertyValues()方式的话，
                 *     类似definition.getPropertyValues().add("interfaceType", beanClazz);
                 *      则要求在FactoryBean（本应用中即ServiceFactory）提供setter方法，否则会注入失败
                 *
                 *    如果采用definition.getConstructorArgumentValues()，
                 *    则FactoryBean中需要提供包含该属性的构造方法，否则会注入失败
                 */
                definition.getConstructorArgumentValues().addGenericArgumentValue(definition.getBeanClassName());
                //注意，这里的BeanClass是生成Bean实例的工厂，不是Bean本身。
                // FactoryBean是一种特殊的Bean，其返回的对象不是指定类的一个实例，
                // 其返回的是该工厂Bean的getObject方法所返回的对象。
                definition.setBeanClass(HttpProxyFactoryBean.class);

                definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
                System.out.println(holder);
            }
        }

        return beanDefinitions;
    }

    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }
}
