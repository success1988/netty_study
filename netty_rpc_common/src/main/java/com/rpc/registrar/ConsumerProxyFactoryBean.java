package com.rpc.registrar;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.DependsOn;

/**
 * @Title：用于创建消费者代理对象的工厂bean
 * @Author：wangchenggong
 * @Date 2021/3/27 9:31
 * @Description
 * @Version
 */
public class ConsumerProxyFactoryBean<T> implements FactoryBean<T> , ApplicationContextAware {

    private ApplicationContext applicationContext;

    private Class<T> rpcConsumerClazz;

    public void setRpcConsumerClazz(Class<T> rpcConsumerClazz) {
        this.rpcConsumerClazz = rpcConsumerClazz;
    }

    @Override
    public T getObject() throws Exception {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(rpcConsumerClazz);
        //enhancer.setCallback(consumerProxyMethodInterceptor);
        enhancer.setCallback(applicationContext.getBean(ConsumerProxyMethodInterceptor.class));
        return (T) enhancer.create();
    }

    @Override
    public Class<?> getObjectType() {
        return rpcConsumerClazz;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
