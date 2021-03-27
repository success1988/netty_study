package com.success.rpc;

import com.rpc.annotations.EnableRpcConsumer;
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
@EnableRpcConsumer(scanPackages = {"com.success.rpc.service"})
public class RpcConsumerApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(RpcConsumerApplication.class);
        application.run(args);
    }

}
