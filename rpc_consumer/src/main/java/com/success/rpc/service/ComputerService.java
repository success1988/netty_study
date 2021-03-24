package com.success.rpc.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Title：代理接口,消费端可以像调用本地方法一下进行调用
 * @Author：wangchenggong
 * @Date 2021/3/23 7:09
 * @Description
 * @Version
 */
@Service
@RequestMapping("/computer")
public interface ComputerService {
    @RequestMapping("/plus")
    public int plus(@RequestParam("a") int a, @RequestParam("b")int b);
}
