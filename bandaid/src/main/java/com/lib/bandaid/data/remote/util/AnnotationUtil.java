package com.lib.bandaid.data.remote.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class AnnotationUtil {

    private AnnotationUtil() {
    }

    public static boolean hasAnnotation(Object obj, Class<? extends Annotation> annotationClass) {
        return obj.getClass().isAnnotationPresent(annotationClass);
    }

    public static List<Annotation[]> getMethodParamsAnnotation(Method method) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        List<Annotation[]> list = new ArrayList<>();
        for (Annotation[] annotations : parameterAnnotations) {
            list.add(annotations);
        }
        return list;
    }

    public static int getMethodParamsAnnotationCount(Method method, Class<? extends Annotation> annotationClass) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        int count = 0;
        for (Annotation[] annotations : parameterAnnotations) {
            for (Annotation annotation : annotations) {
                if (annotation.annotationType() == annotationClass) count++;
            }
        }
        return count;
    }
}
