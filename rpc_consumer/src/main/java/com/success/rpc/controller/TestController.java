package com.success.rpc.controller;

import com.success.rpc.service.ComputerService;
import com.success.rpc.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Title：
 * @Author：wangchenggong
 * @Date 2021/2/27 10:41
 * @Description
 * @Version
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private ComputerService computerService;
    @Autowired
    private HelloService helloService;

    @RequestMapping("/plus")
    public int testPlus(int a, int b){
        helloService.say("早上好");
        return computerService.plus(a,b);
    }
}
