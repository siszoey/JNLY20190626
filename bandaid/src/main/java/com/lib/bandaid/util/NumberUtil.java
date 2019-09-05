package com.lib.bandaid.util;

import androidx.annotation.NonNull;

import java.lang.reflect.Type;
import java.text.DecimalFormat;

/**
 * Created by zy on 2018/10/24.
 */

public final class NumberUtil {

    private NumberUtil() {

    }

    public static boolean isNumber(@NonNull Class<?> clazz) {
        if (clazz == Double.class || clazz == double.class) {
            return true;
        }
        if (clazz == Integer.class || clazz == int.class) {
            return true;
        }
        if (clazz == Long.class || clazz == long.class) {
            return true;
        }
        if (clazz == Short.class || clazz == short.class) {
            return true;
        }
        if (clazz == Byte.class || clazz == byte.class) {
            return true;
        }
        if (clazz == Float.class || clazz == float.class) {
            return true;
        }
        return false;
    }

    public static boolean isNumber(@NonNull Type type) {
        if (type == Double.class || type == double.class) {
            return true;
        }
        if (type == Integer.class || type == int.class) {
            return true;
        }
        if (type == Long.class || type == long.class) {
            return true;
        }
        if (type == Short.class || type == short.class) {
            return true;
        }
        if (type == Byte.class || type == byte.class) {
            return true;
        }
        if (type == Float.class || type == float.class) {
            return true;
        }
        return false;
    }

    public static boolean isNumber(Object o) {
        Number res = 0;
        if (o == null) return false;
        if (o instanceof Double) {
            return true;
        }
        if (o instanceof Integer) {
            return true;
        }
        if (o instanceof Long) {
            return true;
        }
        if (o instanceof Short) {
            return true;
        }
        if (o instanceof Byte) {
            return true;
        }
        if (o instanceof Float) {
            return true;
        }
        return false;
    }

    public static Number obj2Number(Object o) {
        Number res = 0;
        if (o == null) return res;
        if (o instanceof Double) {
            res = (Double) o;
        }
        if (o instanceof Integer) {
            res = (Integer) o;
        }
        if (o instanceof Long) {
            res = (Long) o;
        }
        if (o instanceof Short) {
            res = (Short) o;
        }
        if (o instanceof Byte) {
            res = (Byte) o;
        }
        if (o instanceof Float) {
            res = (Float) o;
        }
        return res;
    }


    /**
     * 将数据保留两位小数
     */
    public static Double getTwoDecimal(double num) {
        DecimalFormat dFormat = new DecimalFormat("#.00");
        String yearString = dFormat.format(num);
        Double temp = Double.valueOf(yearString);
        return temp;
    }

    public static Double division(Double divisorB, Double divisor) {
        if (divisorB == null || divisor == null || divisor == 0) return 0.00;
        return getTwoDecimal(divisorB / divisor);
    }

    public static Double multiplication(Double multi1, Double multi2) {
        if (multi1 == null || multi2 == null) return 0.00;
        return getTwoDecimal(multi1 * multi2);
    }


    public static boolean can2Double(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean can2Float(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Long try2Long(String s) {
        try {
            return Long.parseLong(s);
        } catch (Exception e) {
            return null;
        }
    }

    public static Double try2Double(String s) {
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            return null;
        }
    }

    public static Float try2Float(String s) {
        try {
            return Float.parseFloat(s);
        } catch (Exception e) {
            return null;
        }
    }

    public static Integer try2Integer(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return null;
        }
    }

    public static Short try2Short(String s) {
        try {
            return Short.parseShort(s);
        } catch (Exception e) {
            return null;
        }
    }

    public static Byte try2Bate(String s) {
        try {
            return Byte.parseByte(s);
        } catch (Exception e) {
            return null;
        }
    }
}
