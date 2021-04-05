package com.rpc.client.proxy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rpc.client.NettyClient;
import com.rpc.common.CodeMsgEnum;
import com.rpc.common.RpcRequest;
import com.rpc.common.RpcResponse;
import com.rpc.util.IdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @Title：cglib代理中的方法拦截器，把执行本地方法执行通过网络请求转换为执行远程方法
 * @Author：wangchenggong
 * @Date 2021/3/27 9:32
 * @Description
 * @Version
 */
@Component
public class ConsumerProxyMethodInterceptor implements MethodInterceptor {

    private final Logger logger = LoggerFactory.getLogger(ConsumerProxyMethodInterceptor.class);

    @Autowired
    private NettyClient nettyClient;

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        if(method.getDeclaringClass().equals(Object.class)){
            return method.invoke(this, objects);
        }

        //1.封装请求
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setId(IdGenerator.buildUUID());
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParameterTypes(method.getParameterTypes());
        rpcRequest.setParameters(objects);

        //2.发送请求
        logger.info(">>>发送请求,请求内容为:{}",JSON.toJSONString(rpcRequest));
        RpcResponse rpcResponse =  (RpcResponse)nettyClient.send(rpcRequest);
        logger.info("<<<接收响应,响应内容为:{}",JSON.toJSONString(rpcResponse));

        //3.解析响应结果
        int code = rpcResponse.getCode();
        if(code != CodeMsgEnum.SUCCESS.getCode()){
            logger.error("请求处理发生异常,请求id为:{},异常原因:{}",rpcResponse.getRequestId(), rpcResponse.getErrorMsg());
            throw new RuntimeException(String.format("请求处理发生异常,请求id为:%s,异常原因:%s" ,rpcResponse.getRequestId(),rpcResponse.getErrorMsg()));
        }

        Class<?> returnType = method.getReturnType();
        if(returnType == void.class){
            return null;
        }else if(returnType.isPrimitive() || returnType == String.class){
            return rpcResponse.getData();
        }else{
            return JSONObject.parseObject(JSON.toJSONString(rpcResponse.getData()),returnType);
        }
    }

}
