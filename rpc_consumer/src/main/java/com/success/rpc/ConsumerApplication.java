package com.success.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class ConsumerApplication {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerApplication.class);

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ConsumerApplication.class);
        application.run(args);
        logger.info("The ConsumerApplication server is start!!!");

    }

}
