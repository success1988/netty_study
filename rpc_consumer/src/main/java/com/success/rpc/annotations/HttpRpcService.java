package com.success.rpc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Title： 需要被代理的rpc接口，是一个标记接口
 * @Author：wangchenggong
 * @Date 2021/3/24 15:14
 * @Description
 * @Version
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpRpcService {


}
