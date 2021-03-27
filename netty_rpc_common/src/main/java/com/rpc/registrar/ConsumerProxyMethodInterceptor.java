package com.rpc.registrar;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rpc.netty.client.NettyClient;
import com.rpc.netty.common.RpcRequest;
import com.rpc.netty.common.RpcResponse;
import com.rpc.util.IdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Title：cglib代理中的方法拦截器，把执行本地方法执行通过网络请求转换为执行远程方法
 * @Author：wangchenggong
 * @Date 2021/3/27 9:32
 * @Description
 * @Version
 */
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
        rpcRequest.setClassName(method.getClass().getName());
        rpcRequest.setMethodName(method.getName());

        if(objects != null && objects.length > 0){
            Class<?>[] parameterTypes = new Class<?>[objects.length];
            for (int i = 0; i < objects.length; i++) {
                //FIXME 基本数据类型的Class获取
                parameterTypes[i] = objects[i].getClass();
            }
            rpcRequest.setParameterTypes(parameterTypes);
            rpcRequest.setParameters(objects);
        }


        //2.发送请求
        logger.info(">>>发送请求,请求内容为:{}",JSON.toJSONString(rpcRequest));
        RpcResponse result = (RpcResponse)nettyClient.send(rpcRequest);
        logger.info("<<<接收响应,响应内容为:{}",JSON.toJSONString(result));

        //3.解析响应结果
        int code = result.getCode();
        if(code != 0){
            logger.error("请求处理发生异常,请求id为:{},异常原因:{}",result.getRequestId(), result.getErrorMsg());
            throw new RuntimeException(String.format("请求处理发生异常,请求id为:%s,异常原因:%s" ,result.getRequestId(),result.getErrorMsg()));
        }

        Class<?> returnType = method.getReturnType();
        if(returnType.isPrimitive()){
            //FIXME 基本类型数据转换
            return result.getData();
        }else{
            return JSONObject.parseObject(JSON.toJSONString(result.getData()),returnType);
        }
    }

}
