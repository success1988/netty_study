package com.success.rpc.annotations;

import com.success.rpc.httpproxy.HttpProxyImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Title：用于指定RPC代理接口所在的包
 * @Author：wangchenggong
 * @Date 2021/3/24 16:27
 * @Description  通过@Import注解来调用HttpProxyImportBeanDefinitionRegistrar
 * #registerBeanDefinitions方法，来达到动态注册bean的目的
 * @Version
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(HttpProxyImportBeanDefinitionRegistrar.class)
public @interface EnableHttpRpcConsumer {

    String scanPackage();
}
