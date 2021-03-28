package com.success.rpc.controller;

import com.success.rpc.domain.User;
import com.success.rpc.service.ComputeService;
import com.success.rpc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

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
    @Autowired
    private UserService userService;

    @RequestMapping("/compute")
    public String testCompute(){
        int result1 = computeService.plus(1, 2);
        BigDecimal result2 = computeService.plus(BigDecimal.TEN, BigDecimal.ONE);
        return String.join("###",String.valueOf(result1), result2.toString());
    }

    @RequestMapping("/user")
    public List<User> testUser(){
        User user = new User();
        user.setAge(32);
        user.setName("小明");
        user.setEmail("xiaoming@163.com");
        boolean addResult1 = userService.saveUser(user);

        User user2 = new User();
        user2.setAge(24);
        user2.setName("小红");
        user2.setEmail("xiaohong@163.com");
        boolean addResult2 = userService.saveUser(user2);


        List<User> users = userService.selectAllUsers();
        User targetUser = userService.selectById(1L);

        return users;
    }
}
