package com.rpc.util;

import cn.hutool.core.lang.ClassScanner;
import org.reflections.Reflections;
import org.springframework.util.CollectionUtils;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
        /*Set<Class<?>> resultClazzSet = new HashSet<Class<?>>();
        for (String currentPackage : packages) {
            Reflections reflections = new Reflections(currentPackage);
            Set<Class<?>> currentClazzSet = reflections.getTypesAnnotatedWith(annotation);
            if(currentClazzSet != null && !currentClazzSet.isEmpty()){
                resultClazzSet.addAll(currentClazzSet);
            }
        }
        return resultClazzSet;*/
        Reflections reflections = new Reflections(packages);
        return reflections.getTypesAnnotatedWith(annotation);
    }

    /**
     * 扫描所有的接口类型
     * @param packages
     * @return
     */
    public static Set<Class<?>> scanInterfaces(String[] packages){
        /*Reflections reflections = new Reflections(packages);
        Set<String> initClazzSet = reflections.getAllTypes();

        Set<Class<?>> resultClazzSet = initClazzSet.stream()
                .map(className -> {
                    try {
                        return Class.forName(className);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .filter(clazz -> clazz != null && clazz.isInterface())//只保留接口
                .collect(Collectors.toSet());
        return resultClazzSet;*/
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
