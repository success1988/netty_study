package com.success.rpc;

import com.success.rpc.annotations.EnableHttpRpcConsumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Title：消费者启动类
 * @Author：wangchenggong
 * @Date 2021/2/27 10:31
 * @Description
 * @Version
 */
@SpringBootApplication
@EnableHttpRpcConsumer(scanPackage = "com.success.rpc.service3")
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ConsumerApplication.class);
        //application.setAllowBeanDefinitionOverriding(false);
        application.run(args);
    }
}
