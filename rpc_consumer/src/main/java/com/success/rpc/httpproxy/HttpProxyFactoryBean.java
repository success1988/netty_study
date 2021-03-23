package com.success.rpc.httpproxy;

import org.springframework.beans.factory.FactoryBean;

/**
 * @Title：
 * @Author：wangchenggong
 * @Date 2021/3/23 9:46
 * @Description 创建代理对象
 * @Version
 */
public class HttpProxyFactoryBean<T> implements FactoryBean<T> {



    @Override
    public T getObject() throws Exception {
        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }
}
