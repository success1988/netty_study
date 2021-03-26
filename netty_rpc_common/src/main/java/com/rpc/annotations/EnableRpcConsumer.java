package com.rpc.annotations;

import com.rpc.registrar.ConsumerBeanDefinitionRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Title：
 * @Author：wangchenggong
 * @Date 2021/3/26 15:27
 * @Description
 * @Version
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ConsumerBeanDefinitionRegistrar.class)
public @interface EnableRpcConsumer {

    String[] scanPackages();
}
