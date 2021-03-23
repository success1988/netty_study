package com.success.rpc.httpproxy;

import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Title：
 * @Author：wangchenggong
 * @Date 2021/3/23 9:53
 * @Description 把服务方法的调用转换为对远程服务的http请求
 * @Version
 */
@Component
public class HttpProxyInvocationHandler implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}
