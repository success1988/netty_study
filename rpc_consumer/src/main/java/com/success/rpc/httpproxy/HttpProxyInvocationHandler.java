package com.success.rpc.httpproxy;

import com.success.rpc.util.HttpClientUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @Value("${rpc.url}")
    private String serverUrl;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        }
        Class<?> declaringClass = method.getDeclaringClass();
        RequestMapping masterRequestMapping = declaringClass.getAnnotation(RequestMapping.class);
        
        String methodName = method.getName();
        RequestMapping subRequestMapping = method.getAnnotation(RequestMapping.class);

        String url = serverUrl+ masterRequestMapping.value()[0] + subRequestMapping.value()[0];

        System.out.println("执行"+declaringClass.getSimpleName()+"的"+methodName+"方法,调用url:"+url);
        //HttpClientUtils.postFormData(url, paramMap, null, "UTF-8");

        return null;
    }
}
