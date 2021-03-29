package com.rpc.util;

import cn.hutool.core.lang.ClassScanner;
import org.reflections.Reflections;
import org.springframework.util.CollectionUtils;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * @Title：注解扫描器
 * @Author：wangchenggong
 * @Date 2021/3/26 17:41
 * @Description
 * @Version
 */
public class ClazzScanner {

    /**
     * 在指定的多个包里扫描指定的注解，获取Class集合
     * @param packages
     * @param annotation
     * @return
     */
    public static Set<Class<?>> scanClassWithAnnotation(String[] packages, Class<? extends Annotation> annotation){
        Reflections reflections = new Reflections(packages);
        return reflections.getTypesAnnotatedWith(annotation);
    }

    /**
     * 扫描指定包下所有的接口类型，获取Class集合
     * @param packages
     * @return
     */
    public static Set<Class<?>> scanInterfaces(String[] packages){
        Set<Class<?>> resultClazzSet = new HashSet<Class<?>>();
        for (String currentPackage : packages) {
            Set<Class<?>> currentClazzSet = ClassScanner.scanPackage(currentPackage, item -> item.isInterface());
            if(!CollectionUtils.isEmpty(currentClazzSet)){
                resultClazzSet.addAll(currentClazzSet);
            }
        }
        return resultClazzSet;
    }

}
