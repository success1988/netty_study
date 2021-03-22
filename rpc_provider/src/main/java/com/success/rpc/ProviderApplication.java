package com.success.rpc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Title：
 * @Author：wangchenggong
 * @Date 2021/3/22 22:44
 * @Description
 * @Version
 */
@SpringBootApplication
public class ProviderApplication {


    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ProviderApplication.class);
        application.run(args);
    }

}
