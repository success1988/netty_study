package com.success.rpc.service.impl;

import com.rpc.annotations.RpcProvider;
import com.success.rpc.domain.User;
import com.success.rpc.service.UserService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

/**
 * @Title：
 * @Author：wangchenggong
 * @Date 2021/3/26 16:05
 * @Description
 * @Version
 */
@RpcProvider
public class UserServiceImpl implements UserService {

    private ConcurrentHashMap<Long,User> userMap = new ConcurrentHashMap<>();
    private LongAdder longAdder = new LongAdder();

    @Override
    public boolean saveUser(User user) {
        longAdder.add(1);
        user.setId(longAdder.longValue());
        userMap.put(user.getId(), user);
        return true;
    }

    @Override
    public List<User> selectAllUsers() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public User selectById(Long id) {
        return userMap.get(id);
    }
}
