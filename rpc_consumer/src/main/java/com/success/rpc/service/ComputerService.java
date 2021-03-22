package com.success.rpc.service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Title：
 * @Author：wangchenggong
 * @Date 2021/3/23 7:09
 * @Description
 * @Version
 */
@RequestMapping("/computer")
public interface ComputerService {
    @RequestMapping("/plus")
    public int plus(@RequestParam("a") int a, @RequestParam("b")int b);
}
