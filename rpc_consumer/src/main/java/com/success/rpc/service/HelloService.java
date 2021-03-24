package com.success.rpc.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Title：
 * @Author：wangchenggong
 * @Date 2021/3/24 7:04
 * @Description
 * @Version
 */
@Service
@RequestMapping("/hello")
public interface HelloService {
    @RequestMapping("/say")
    public String say(@RequestParam("content") String content);
}

