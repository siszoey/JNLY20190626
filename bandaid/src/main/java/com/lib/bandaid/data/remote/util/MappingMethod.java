package com.lib.bandaid.data.remote.util;

import android.content.Context;
import android.os.Build;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;

import androidx.collection.LongSparseArray;
import androidx.collection.SimpleArrayMap;

import com.lib.bandaid.data.remote.mock.annotation.DeleteMapping;
import com.lib.bandaid.data.remote.mock.annotation.GetMapping;
import com.lib.bandaid.data.remote.mock.annotation.PatchMapping;
import com.lib.bandaid.data.remote.mock.annotation.PostMapping;
import com.lib.bandaid.data.remote.mock.annotation.PutMapping;
import com.lib.bandaid.data.remote.mock.annotation.RequestMapping;
import com.lib.bandaid.data.remote.mock.annotation.RestController;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zy on 2017/5/5.
 */
public class MappingMethod {

    private static MappingMethod ourInstance;

    private Map<String, Method> RequestMapping;

    public static MappingMethod getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new MappingMethod(context);
        }
        return ourInstance;
    }

    public static MappingMethod getInstance() {
        return ourInstance;
    }

    public MappingMethod(Context context) {
        RequestMapping = new HashMap<>();
        scanPackage(context);
    }

    private void scanPackage(Context context) {
        String packagePath = context.getPackageName();
        Scanner.scan(context,packagePath, new Scanner.IWhat() {

            @Override
            public void execute(Class clazz) {
                if (clazz != null && clazz.isAnnotationPresent(RestController.class)) {
                    String[] classValue = null, methodValue = null;
                    if (clazz.isAnnotationPresent(RequestMapping.class)) {
                        RequestMapping reqMapping = (RequestMapping) clazz.getAnnotation(RequestMapping.class);
                        classValue = reqMapping.value();
                    }

                    Method[] methods = clazz.getDeclaredMethods();
                    Method method;
                    for (int i = 0; i < methods.length; i++) {
                        method = methods[i];
                        if (method.isAnnotationPresent(GetMapping.class)) {
                            GetMapping mapping = method.getAnnotation(GetMapping.class);
                            methodValue = mapping.value();
                            String url = "";
                            if (!isEmpty(classValue)) url += classValue[0];
                            if (!isEmpty(methodValue)) url += methodValue[0];
                            if (RequestMapping.containsKey(url)) new Throwable(url + "出现重复,请检查！");
                            RequestMapping.put(url, method);
                        }
                        if (method.isAnnotationPresent(PostMapping.class)) {
                            PostMapping mapping = method.getAnnotation(PostMapping.class);
                            methodValue = mapping.value();
                            String url = "";
                            if (!isEmpty(classValue)) url += classValue[0];
                            if (!isEmpty(methodValue)) url += methodValue[0];
                            if (RequestMapping.containsKey(url)) new Throwable(url + "出现重复,请检查！");
                            RequestMapping.put(url, method);
                        }
                        if (method.isAnnotationPresent(PutMapping.class)) {
                            PutMapping mapping = method.getAnnotation(PutMapping.class);
                            methodValue = mapping.value();
                            String url = "";
                            if (!isEmpty(classValue)) url += classValue[0];
                            if (!isEmpty(methodValue)) url += methodValue[0];
                            if (RequestMapping.containsKey(url)) new Throwable(url + "出现重复,请检查！");
                            RequestMapping.put(url, method);
                        }
                        if (method.isAnnotationPresent(DeleteMapping.class)) {
                            DeleteMapping mapping = method.getAnnotation(DeleteMapping.class);
                            methodValue = mapping.value();
                            String url = "";
                            if (!isEmpty(classValue)) url += classValue[0];
                            if (!isEmpty(methodValue)) url += methodValue[0];
                            if (RequestMapping.containsKey(url)) new Throwable(url + "出现重复,请检查！");
                            RequestMapping.put(url, method);
                        }
                        if (method.isAnnotationPresent(PatchMapping.class)) {
                            PatchMapping mapping = method.getAnnotation(PatchMapping.class);
                            methodValue = mapping.value();
                            String url = "";
                            if (!isEmpty(classValue)) url += classValue[0];
                            if (!isEmpty(methodValue)) url += methodValue[0];
                            if (RequestMapping.containsKey(url)) new Throwable(url + "出现重复,请检查！");
                            RequestMapping.put(url, method);
                        }
                    }
                }
            }

            @Override
            public void end() {
                System.out.println("扫描包结束");
            }
        });
    }

    public Map<String, Method> getRequestMapping() {
        return RequestMapping;
    }

    private boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof CharSequence && obj.toString().length() == 0) {
            return true;
        }
        if (obj.getClass().isArray() && Array.getLength(obj) == 0) {
            return true;
        }
        if (obj instanceof Collection && ((Collection) obj).isEmpty()) {
            return true;
        }
        if (obj instanceof Map && ((Map) obj).isEmpty()) {
            return true;
        }
        if (obj instanceof SimpleArrayMap && ((SimpleArrayMap) obj).isEmpty()) {
            return true;
        }
        if (obj instanceof SparseArray && ((SparseArray) obj).size() == 0) {
            return true;
        }
        if (obj instanceof SparseBooleanArray && ((SparseBooleanArray) obj).size() == 0) {
            return true;
        }
        if (obj instanceof SparseIntArray && ((SparseIntArray) obj).size() == 0) {
            return true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (obj instanceof SparseLongArray && ((SparseLongArray) obj).size() == 0) {
                return true;
            }
        }
        if (obj instanceof LongSparseArray && ((LongSparseArray) obj).size() == 0) {
            return true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (obj instanceof android.util.LongSparseArray
                    && ((android.util.LongSparseArray) obj).size() == 0) {
                return true;
            }
        }
        return false;
    }
}
