package com.rpc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Title：rpc消费方接口标记
 * @Author：wangchenggong
 * @Date 2021/3/26 15:15
 * @Description
 * @Version
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcConsumer {
}
