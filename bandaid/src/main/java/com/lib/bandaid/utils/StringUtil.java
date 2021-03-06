package com.lib.bandaid.utils;

import android.os.Build;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;

import androidx.annotation.NonNull;
import androidx.collection.LongSparseArray;
import androidx.collection.SimpleArrayMap;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class StringUtil {

    public static String completeZero(@NonNull Object obj, int len) {
        int length = (obj + "").length();
        StringBuffer sb = new StringBuffer();
        if (length <= len) {
            for (int i = 0; i < len - length; i++) {
                sb.append("0");
            }
            sb.append(obj);
        }
        return sb.toString();
    }


    /**
     * 显示时候使用
     *
     * @param obj
     * @return
     */
    public static String removeNull(Object obj) {
        if (obj == null) return "";
        else return obj.toString();
    }

    /**
     * 保存时候使用
     *
     * @param obj
     * @return
     */
    public static String removeEmpty(String obj) {
        if ("".equals(obj)) return null;
        else return obj;
    }

    private static List<Character> arrayList = new ArrayList<>();

    public static boolean checkPsw(String value) {


        if (value.length() < 6) {
            return false;
        }

        boolean zimu = false;
        boolean shuzi = false;
        boolean tszf = false;

        arrayList.add('!');
        arrayList.add('@');
        arrayList.add('#');
        arrayList.add('$');
        arrayList.add('%');
        arrayList.add('^');
        arrayList.add('&');
        arrayList.add('*');


        for (int i = 0; i < value.length(); i++) {
            char ca = value.charAt(i);
            if ((ca >= 'a' && ca <= 'z') || (ca >= 'A' && ca <= 'Z')) {
                zimu = true;
                continue;
            }

            if (ca >= '0' && ca <= '9') {
                shuzi = true;
                continue;
            }

            if (arrayList.contains(ca)) {
                tszf = true;
                continue;
            }

            return false;
        }
        return (zimu && shuzi && tszf);
    }

    /**
     * 数据为空检查
     */
    public static boolean isEmpty(final Object obj) {
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
