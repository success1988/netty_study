package com.success.rpc.httpproxy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.success.rpc.util.HttpClientUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
        Class<?> declaringClass = method.getDeclaringClass();
        if (Object.class.equals(declaringClass)) {
            return method.invoke(this, args);
        }

        String methodName = method.getName();
        RequestMapping masterRequestMapping = declaringClass.getAnnotation(RequestMapping.class);
        RequestMapping subRequestMapping = method.getAnnotation(RequestMapping.class);

        //拼接请求地址
        String url = serverUrl+ masterRequestMapping.value()[0] + subRequestMapping.value()[0];
        //组装请求参数
        Map<String, String> paramMap = new HashMap<>();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            Annotation[] parameterAnnotation = parameterAnnotations[i];
            RequestParam annotation = (RequestParam)parameterAnnotation[0];
            String key = annotation.value();
            paramMap.put(key, String.valueOf(args[i]));
        }
        System.out.println(">>>>执行"+declaringClass.getSimpleName()+"的"+methodName+"方法,调用url:"+url+"，请求参数为:"+paramMap);
        String result = HttpClientUtils.postFormData(url, paramMap, null, "UTF-8");
        System.out.println(">>>"+url+"的响应结果为:"+result);

        //将响应结果转换为接口方法的返回值类型
        Class<?> returnType = method.getReturnType();
        if (returnType.isPrimitive() || String.class.isAssignableFrom(returnType)){
            if(returnType == int.class || returnType == Integer.class){
                return Integer.valueOf(result);
            }else if(returnType == long.class || returnType == Long.class){
                return Long.valueOf(result);
            }
            return result;
        }else if (Collection.class.isAssignableFrom(returnType)){
            return JSONArray.parseArray(result,Object.class);
        }else if(Map.class.isAssignableFrom(returnType)){
            return JSON.parseObject(result,Map.class);
        }else{
            return JSONObject.parseObject(result, returnType);
        }
    }
}
