package com.rpc.annotations;

import com.rpc.registrar.ProviderBeanDefinitionRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Title：
 * @Author：wangchenggong
 * @Date 2021/3/26 15:26
 * @Description
 * @Version
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ProviderBeanDefinitionRegistrar.class)
public @interface EnableRpcProvider {

    String[] scanPackages();
}
