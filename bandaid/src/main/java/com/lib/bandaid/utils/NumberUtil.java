package com.lib.bandaid.utils;

import java.text.DecimalFormat;

/**
 * Created by zy on 2018/10/24.
 */

public final class NumberUtil {

    private NumberUtil() {
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

    public static Double division(Double divisorB, Double divisor){
        if(divisorB == null ||divisor==null||divisor==0)return 0.00;
        return getTwoDecimal(divisorB/divisor);
    }

    public static Double multiplication(Double multi1, Double multi2){
        if(multi1 == null ||multi2==null)return 0.00;
        return getTwoDecimal(multi1*multi2);
    }
}
