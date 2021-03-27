package com.success.rpc.controller;

import com.success.rpc.service.ComputeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @Title：测试类
 * @Author：wangchenggong
 * @Date 2021/3/27 17:24
 * @Description
 * @Version
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private ComputeService computeService;

    @RequestMapping("/compute")
    public String testCompute(){
        int result1 = computeService.plus(1, 2);
        BigDecimal result2 = computeService.plus(BigDecimal.TEN, BigDecimal.ONE);
        return String.join("###",String.valueOf(result1), result2.toString());
    }
}
