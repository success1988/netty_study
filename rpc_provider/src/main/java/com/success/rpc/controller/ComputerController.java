package com.success.rpc.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Title：
 * @Author：wangchenggong
 * @Date 2021/3/22 22:40
 * @Description
 * @Version
 */
@RestController
@RequestMapping("/computer")
public class ComputerController {

    @RequestMapping("/plus")
    public int testPlus(int a, int b){
        return a+b;
    }
}

