package com.success.rpc.service.impl;

import com.rpc.annotations.RpcProvider;
import com.success.rpc.domain.User;
import com.success.rpc.service.UserService;

import java.util.List;

/**
 * @Title：
 * @Author：wangchenggong
 * @Date 2021/3/26 16:05
 * @Description
 * @Version
 */
@RpcProvider
public class UserServiceImpl implements UserService {
    @Override
    public boolean saveUser(User user) {
        return false;
    }

    @Override
    public List<User> selectAllUsers() {
        return null;
    }

    @Override
    public User selectById(Long id) {
        return null;
    }
}
