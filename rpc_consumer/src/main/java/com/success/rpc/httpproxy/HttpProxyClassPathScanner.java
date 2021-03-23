package com.success.rpc.httpproxy;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

import java.util.Set;

/**
 * @Title：
 * @Author：wangchenggong
 * @Date 2021/3/23 10:28
 * @Description
 * @Version
 */
public class HttpProxyClassPathScanner extends ClassPathBeanDefinitionScanner {

    public HttpProxyClassPathScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {


        return super.doScan(basePackages);
    }
}
