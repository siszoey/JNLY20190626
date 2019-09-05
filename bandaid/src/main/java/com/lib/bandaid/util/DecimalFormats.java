package com.lib.bandaid.util;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public final class DecimalFormats {

    private static Map<String, DecimalFormat> cache = new HashMap<>();

    public static DecimalFormat getFormat(String pattern) {
        DecimalFormat format = cache.get(pattern);
        if (format == null) format = new DecimalFormat(pattern);
        cache.put(pattern, format);
        return format;
    }

}
