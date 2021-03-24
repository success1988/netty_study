package com.success.rpc.controller;

import com.success.rpc.components.MyComponent;
import com.success.rpc.components.MyUser;
import com.success.rpc.service.ComputerService;
import com.success.rpc.service.HelloService;
import com.success.rpc.service2.HelloService2;
import com.success.rpc.service3.HelloService3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Title：通过自动装配代理接口，来测试代理接口
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
    @Autowired
    private HelloService2 helloService2;
    @Autowired
    private HelloService3 helloService3;

    @Autowired
    private MyUser myUser;
    @Autowired
    private MyComponent myComponent;

    @RequestMapping("/plus")
    public int testPlus(int a, int b){
        return computerService.plus(a,b);
    }

    @RequestMapping("/hello")
    public String sayHello(){
        String name = myUser.getName()+myComponent.test();

        String s1 = helloService.say("早上好");
        String s2 = helloService2.say("下午好");
        String s3 = helloService3.say("晚上好");

        return String.join("###",name, s1,s2,s3);
    }
}
