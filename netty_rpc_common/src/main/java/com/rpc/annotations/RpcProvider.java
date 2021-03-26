package com.rpc.annotations;

import org.springframework.stereotype.Service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Title：
 * @Author：wangchenggong
 * @Date 2021/3/26 15:16
 * @Description
 * @Version
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcProvider {


}
