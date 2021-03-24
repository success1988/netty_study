package com.success.rpc.httpproxy;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Proxy;

/**
 * @Title：相对于BeanFactory这个大工厂,这是一个小工厂，专门用于创建某种类型的bean(默认创建的是单例bean)
 * @Author：wangchenggong
 * @Date 2021/3/23 9:46
 * @Description 创建代理对象
 * @Version
 */
public class HttpProxyFactoryBean<T> implements FactoryBean<T> {

    /**
     *【注意】
     * 这里之所以可以进行自动装配，是因为当前的这个HttpProxyFactoryBean是会被注册到Spring中的
     * 只不过它的注册方式 跟一般的不一样（一般会在类上，加一个如@Component、@Service这样的注解 ）
     * 它是通过注册BeanDefinition的方式注册的，可能会注册多个，而其中的每一个HttpProxyFactoryBean实例都会被自动装配同一个HttpProxyInvocationHandler实例
     *
     * 也有等价的做法是：
     * 利用ApplicationContextAware接口的setApplicationContext获取到applicationContext，
     * 然后把applicationContext 作为属性设置到当前类中
     *  再利用applicationContext的getBean方法来获取InvocationHandler的实例
     */
    @Autowired
    private HttpProxyInvocationHandler httpProxyInvocationHandler;

    private Class<T> rpcInterface;

    public HttpProxyFactoryBean(Class<T> rpcInterface){
        this.rpcInterface = rpcInterface;
    }


    @Override
    public T getObject() throws Exception {
        //这里应该放ComputerService接口
        return (T)Proxy.newProxyInstance(rpcInterface.getClassLoader(),new Class[]{rpcInterface} ,httpProxyInvocationHandler);
    }

    @Override
    public Class<?> getObjectType() {
        return rpcInterface;
    }

}
