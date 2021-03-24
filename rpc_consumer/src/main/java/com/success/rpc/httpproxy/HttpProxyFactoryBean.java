package com.success.rpc.httpproxy;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;

/**
 * @Title：
 * @Author：wangchenggong
 * @Date 2021/3/23 9:46
 * @Description 创建代理对象
 * @Version
 */
public class HttpProxyFactoryBean<T> implements FactoryBean<T> {

    @Autowired
    private HttpProxyInvocationHandler httpProxyInvocationHandler;

    private Class<T> rpcInterface;

    public HttpProxyFactoryBean(Class<T> rpcInterface){
        this.rpcInterface = rpcInterface;
    }


    @Override
    public T getObject() throws Exception {
        //这里应该放ComputerService接口
        return (T)Proxy.newProxyInstance(rpcInterface.getClassLoader(),new Class[]{rpcInterface} ,httpProxyInvocationHandler);
    }

    @Override
    public Class<?> getObjectType() {
        return rpcInterface;
    }
}
