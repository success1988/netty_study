package com.rpc.util;

import java.util.UUID;

/**
 * @Title：ID生成工具类
 * @Author：wangchenggong
 * @Date 2021/3/27 15:51
 * @Description
 * @Version
 */
public class IdGenerator {

    public static String buildUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
