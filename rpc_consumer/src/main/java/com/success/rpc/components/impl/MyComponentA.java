package com.success.rpc.components.impl;

import com.success.rpc.components.MyComponent;
import org.springframework.stereotype.Component;

/**
 * @Title：
 * @Author：wangchenggong
 * @Date 2021/3/24 17:38
 * @Description
 * @Version
 */
@Component
public class MyComponentA implements MyComponent {
    @Override
    public String test() {
        return "MyComponentA";
    }
}
