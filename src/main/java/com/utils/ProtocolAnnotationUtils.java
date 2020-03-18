package com.utils;

import com.easyim.Application;
import org.apache.rocketmq.remoting.common.Pair;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProtocolAnnotationUtils {

    public static <A extends Annotation> List<Pair<Object, A>> getBeansWithAnnotation(Class<A> annotationClass){
        List<String> beansName = Arrays.asList(Application.getContext().getBeanDefinitionNames());
        List<Object> beans = new ArrayList<>();
        beansName.forEach(v-> {
            beans.add(Application.getContext().getBean(v));
        });

        return beans.stream()
                .filter(v-> AnnotationUtils.findAnnotation(v.getClass(), annotationClass) != null)
                .map(vv->new Pair<>(vv, AnnotationUtils.findAnnotation(vv.getClass(), annotationClass)))
                .collect(Collectors.toList());
    }

    public static  <A extends Annotation> List<Pair<Method, A>> getMethodWithAnnotation(Class<A> annotationClass, Class clz){
        String excludeMethodKey = "EnhancerBySpringCGLIB";
        return Arrays.stream(ReflectionUtils.getAllDeclaredMethods(clz))
                .filter(v->!v.toString().contains(excludeMethodKey)) // 不返回cglib代理方法
                .filter(v->AnnotationUtils.findAnnotation(v, annotationClass) != null)
                .map(vv->new Pair<>(vv, AnnotationUtils.findAnnotation(vv, annotationClass)))
                .collect(Collectors.toList());
    }

    public static Object invokeMethod(Object bean, Method method, Object[] params) {
        Class<? extends Object>[] paramClass = null;
        if (params != null) {
            int paramsLength = params.length;
            paramClass = new Class[paramsLength];
            for (int i = 0; i < paramsLength; i++) {
                paramClass[i] = params[i].getClass();
            }
        }
        // 执行方法
        return ReflectionUtils.invokeMethod(method, bean, params);
    }

    // 获取方法的实例化参数
    @Nullable
    public static List<Object> getMethodInstanceParam(Method method){
        Class<?>[] classes = method.getParameterTypes();
        List<Object> parameterForMethod = new ArrayList<>();
        Arrays.asList(classes).forEach(c->{
            Object parameter = null;
            try {
                Constructor constructor = c.getConstructor();
                parameter = constructor.newInstance();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            finally {
                if (parameter != null){
                    parameterForMethod.add(parameter);
                }
            }
        });

        return (method.getParameterCount() == parameterForMethod.size()) ? parameterForMethod : null;
    }

    // 若方法参数上存在注解，返回参数类型
    @Nullable
    public static <A extends Annotation> Class getParamClzWithAnnotationFor(Method method, Class<A> annotationClz){
        Class<?>[] paramClz = method.getParameterTypes();
        Annotation[][] annotations = method.getParameterAnnotations();
        for(int i = 0; i < annotations.length; i++){
            if(annotations[i].length == 0){
                continue;
            }
            for(int j = 0; j < annotations[i].length; j++){
                if(annotations[i][j].annotationType().equals(annotationClz)){
                    return paramClz[i];
                }
            }
        }

        return null;
    }
}
