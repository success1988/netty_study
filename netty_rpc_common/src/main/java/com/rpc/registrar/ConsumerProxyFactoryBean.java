package com.rpc.registrar;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.Enhancer;

/**
 * @Title：用于创建消费者代理对象的工厂bean
 * @Author：wangchenggong
 * @Date 2021/3/27 9:31
 * @Description
 * @Version
 */
public class ConsumerProxyFactoryBean<T> implements FactoryBean<T> {

    @Autowired
    private ConsumerProxyMethodInterceptor consumerProxyMethodInterceptor;

    private Class<T> rpcConsumerClazz;

    public void setRpcConsumerClazz(Class<T> rpcConsumerClazz) {
        this.rpcConsumerClazz = rpcConsumerClazz;
    }

    @Override
    public T getObject() throws Exception {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(rpcConsumerClazz);
        enhancer.setCallback(consumerProxyMethodInterceptor);
        return (T) enhancer.create();
    }

    @Override
    public Class<?> getObjectType() {
        return rpcConsumerClazz;
    }
}
