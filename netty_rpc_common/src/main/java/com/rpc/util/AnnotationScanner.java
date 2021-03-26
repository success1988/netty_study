package com.rpc.util;

import org.reflections.Reflections;

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
public class AnnotationScanner {

    /**
     * 在指定的多个包里扫描指定的注解，获取Class集合
     * @param packages
     * @param annotation
     * @return
     */
    public static Set<Class<?>> scanClassWithAnnotation(String[] packages, Class<? extends Annotation> annotation){
        Set<Class<?>> resultClazzSet = new HashSet<Class<?>>();
        for (String currentPackage : packages) {
            Reflections reflections = new Reflections(currentPackage);
            Set<Class<?>> currentClazzSet = reflections.getTypesAnnotatedWith(annotation);
            if(currentClazzSet != null && !currentClazzSet.isEmpty()){
                resultClazzSet.addAll(currentClazzSet);
            }
        }
        return resultClazzSet;
    }
}
