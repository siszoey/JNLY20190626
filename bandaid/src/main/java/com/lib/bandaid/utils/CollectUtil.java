package com.lib.bandaid.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zy on 2017/6/17.
 * 数组与集合之间的转换
 */

public final class CollectUtil {

    private CollectUtil() {

    }

    /**
     * @param objects
     * @param obj
     * @return
     */
    public static Object[] addArray(Object obj, Object[] objects) {
        if (objects == null) return new Object[]{obj};
        Object[] _objects = new Object[objects.length + 1];
        _objects[0] = obj;
        for (int i = 1; i <= objects.length; i++) {
            _objects[i] = objects[i - 1];
        }
        return _objects;
    }

    public static Object[] arrayAdd(Object[] objects, Object obj) {
        if (objects == null) return new Object[]{obj};
        Object[] _objects = new Object[objects.length + 1];
        for (int i = 0; i < objects.length; i++) {
            _objects[i] = objects[i];
        }
        _objects[objects.length] = obj;
        return _objects;
    }

    /**
     * 集合转数组
     *
     * @param objects
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T[] arrayObj2T(Object[] objects, Class clazz) {
        T[] TArray = null;
        if (objects != null && objects.length > 0) {
            TArray = (T[]) Array.newInstance(clazz, objects.length);
            for (int i = 0; i < objects.length; i++) {
                Array.set(TArray, i, objects[i]);
            }
        }
        return TArray;
    }

    /**
     * 集合转数组
     *
     * @param list
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T[] list2TArray(List list, Class clazz) {
        T[] TArray = null;
        if (list != null && list.size() > 0) {
            TArray = (T[]) Array.newInstance(clazz, list.size());
            for (int i = 0; i < list.size(); i++) {
                Array.set(TArray, i, list.get(i));
            }
        }
        return TArray;
    }


    /**
     * 集合转数组
     *
     * @param list
     * @return
     */
    public static int[] list2TArray(List list) {
        int[] TArray = null;
        if (list != null && list.size() > 0) {
            TArray = new int[list.size()];
            for (int i = 0; i < list.size(); i++) {
                TArray[i] = (int) list.get(i);
            }
        }
        return TArray;
    }


    /**
     * 集合转数组
     *
     * @param list
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T[] tArray2List(List list, Class clazz) {
        T[] TArray = null;
        if (list != null && list.size() > 0) {
            TArray = (T[]) Array.newInstance(clazz, list.size());
            for (int i = 0; i < list.size(); i++) {
                Array.set(TArray, i, list.get(i));
            }
        }
        return TArray;
    }

    /**
     * 数组转集合
     *
     * @param array
     * @param <T>
     * @return
     */
    public static <T> List<T> array2List(T[] array) {
        List<T> list = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            T t = array[i];
            list.add(t);
        }
        return list;
    }

    public static <T> List<Object> ListT2ListObj(List<T> list) {
        List<Object> objList = new ArrayList<>();
        for (Object e : list) {
            Object obj = (Object) e;
            objList.add(obj);
        }
        return objList;
    }


    public static <T> T[] removeNullFromArray(T[] array, Class clazz) {
        if (array == null) return null;
        List<T> list = null;
        T t = null;
        for (int i = 0; i < array.length; i++) {
            t = array[i];
            if (list == null) {
                list = new ArrayList<>();
            }
            if (t != null) {
                list.add(t);
            }
        }
        if (list == null) return null;
        return tArray2List(list, clazz);
    }

    /**
     * origin 从 list里  找到自己（指定几个属性），并把自己替换
     *
     * @param origin
     * @param list
     * @param <T>
     * @return
     */
    public static <T> T queryListByTFields(T origin, List<T> list, Map<String, String> fields) {
        T t;
        for (int i = 0, len = list.size(); i < len; i++) {
            t = list.get(i);
            if (ObjectUtil.objFieldsIsEqual(origin, t, fields)) {
                origin = t;
                break;
            }
        }
        return origin;
    }

    /**
     * f1,f2 是两个相同类的实例     (map判断)
     *
     * @param origin
     * @param f1
     * @param list
     * @param f2
     * @param fields
     * @param <T>
     * @return
     */
    public static <T> List<T> replaceTObjVal(List<T> origin, String f1, List list, String f2, Map<String, String> fields) {
        T o1, of1;
        Object o2, of2;
        for (int i = 0, lenI = origin.size(); i < lenI; i++) {
            o1 = origin.get(i);
            if (f1 == null) {
                of1 = o1;
            } else {
                of1 = (T) ReflectUtil.getFieldValue(o1, f1);
            }
            for (int j = 0, lenJ = list.size(); j < lenJ; j++) {
                o2 = list.get(j);
                if (f2 == null) {
                    of2 = o2;
                } else {
                    of2 = ReflectUtil.getFieldValue(o2, f2);
                }
                if (ObjectUtil.objFieldsIsEqual(of1, of2, fields)) {
                    ReflectUtil.setFieldValue(o1, f1, of2);
                    break;
                }
            }
        }
        return origin;
    }

    /**
     * origin 和 other 有相同类的实例 现在给他替换了
     *
     * @param origin
     * @param f1
     * @param other
     * @param f2
     * @param <T>
     * @return
     */
    public static <T> T replaceTObjVal(T origin, String f1, Object other, String f2) {
        T o1, of1;
        Object o2;
        o2 = ReflectUtil.getFieldValue(other, f2);
        ReflectUtil.setFieldValue(origin, f1, o2);
        return origin;
    }


    public static <T> List<T> getListForList(List list, String field) {
        Object item;
        List<T> res = new ArrayList<>();
        List<T> ts;
        for (int i = 0, len = list.size(); i < len; i++) {
            item = list.get(i);
            ts = (List<T>) ReflectUtil.getFieldValue(item, field);
            res.addAll(ts);
        }
        return res;
    }
}
