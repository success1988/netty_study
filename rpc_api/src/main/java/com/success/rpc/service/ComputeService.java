package com.success.rpc.service;

import java.math.BigDecimal;

/**
 * @Title：计算服务
 * @Author：wangchenggong
 * @Date 2021/2/27 10:07
 * @Description
 * @Version
 */
public interface ComputeService {

    /**
     * 计算两整数之和
     * @param a
     * @param b
     * @return 和
     */
    public int plus(int a, int b);

    /**
     * 计算两数之和
     * @param a
     * @param b
     * @return
     */
    public BigDecimal plus(BigDecimal a, BigDecimal b);
}
