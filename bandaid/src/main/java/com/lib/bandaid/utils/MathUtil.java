package com.lib.bandaid.utils;

import java.text.DecimalFormat;

/**
 * Created by zy on 2019/4/23.
 */

public final class MathUtil {

    MathUtil() {
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

    /**
     * 将数据保留两位小数
     */
    static DecimalFormat dFormat = new DecimalFormat("#.00");

    public static Double getTwoDecimal(double num) {
        String s = dFormat.format(num);
        return Double.valueOf(s);
    }
}
