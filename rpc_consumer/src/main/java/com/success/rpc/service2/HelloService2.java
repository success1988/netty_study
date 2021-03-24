package com.success.rpc.service2;

import com.success.rpc.annotations.HttpRpcService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Title：代理接口,消费端可以像调用本地方法一下进行调用
 * @Author：wangchenggong
 * @Date 2021/3/24 15:19
 * @Description
 * @Version
 */
@HttpRpcService
@RequestMapping("/hello")
public interface HelloService2 {
    @RequestMapping("/say2")
    public String say(@RequestParam("content") String content);
}
