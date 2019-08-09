package com.titan.jnly.vector.util;


import java.util.Map;

public final class PropertyUtil {

    private final static String[] USEFUL_FIELD = new String[]{
            "TREE_TYPE",
            "XIAN",
            "XIANG",
            "CUN",
            "XDM",
            "SZZWM",
            "SZSM",
            "SZLDM",
            "KE",
            "SHU",
            "SZCS",
            "FBTD",
            "SZQY",
            "GSDJ",
            "POXIANG",
            "PDDJ",
            "POWEI",
            "TRLX",

            "SZS",
            "SZHJ",
            "XZGSMMYY",
            "QUANSHU",
            "BHXZ",
            "YHFZXZ",
    };

    public static void copyUseFulAttr(Map<String, Object> last, Map<String, Object> property) {
        if (USEFUL_FIELD == null || USEFUL_FIELD.length == 0) return;
        for (String field : USEFUL_FIELD) {
            property.put(field, last.get(field));
        }
    }

}
