package com.success.rpc.service;

import com.success.rpc.domain.User;

import java.util.List;

/**
 * @Title：用户服务接口
 * @Author：wangchenggong
 * @Date 2021/3/26 16:00
 * @Description
 * @Version
 */
public interface UserService {

    /**
     * 新增用户
     * @param user
     * @return
     */
    boolean saveUser(User user);

    /**
     * 查询所有用户列表
     * @return
     */
    List<User> selectAllUsers();

    /**
     * 根据id查询某个用户信息
     * @param id
     * @return
     */
    User selectById(Long id);

    /**
     * 自我介绍
     * @param name
     * @param age
     */
    void say(String name, int age);
}
