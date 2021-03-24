package com.success.rpc.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Title：接口服务
 * @Author：wangchenggong
 * @Date 2021/3/22 22:40
 * @Description
 * @Version
 */
@RestController
@RequestMapping("/hello")
public class HelloController {

    @RequestMapping("/say")
    public String say(String content){
        return "hello,"+content;
    }

    @RequestMapping("/say2")
    public String say2(String content){
        return "Hi,"+content;
    }

    @RequestMapping("/say3")
    public String say3(String content){
        return "喂,"+content;
    }
}

