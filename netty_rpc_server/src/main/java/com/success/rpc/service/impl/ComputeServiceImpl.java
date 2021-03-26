package com.success.rpc.service.impl;

import com.rpc.annotations.RpcProvider;
import com.success.rpc.service.ComputeService;

import java.math.BigDecimal;

/**
 * @Title：计算服务实现
 * @Author：wangchenggong
 * @Date 2021/3/26 15:46
 * @Description
 * @Version
 */
@RpcProvider
public class ComputeServiceImpl implements ComputeService {


    @Override
    public int plus(int a, int b) {
        return a+b;
    }

    @Override
    public BigDecimal plus(BigDecimal a, BigDecimal b) {
        return a.add(b);
    }
}
