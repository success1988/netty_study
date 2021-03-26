package com.success.rpc;

import com.rpc.annotations.EnableRpcProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Title：
 * @Author：wangchenggong
 * @Date 2021/3/26 15:44
 * @Description
 * @Version
 */
@SpringBootApplication
@EnableRpcProvider(scanPackages = {"com.success.rpc.service"})
public class RpcServerApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(RpcServerApplication.class);
        application.run(args);
    }

}
