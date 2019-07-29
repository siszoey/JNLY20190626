package com.lib.bandaid.widget.easyui.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则验证
 */
public final class RegexUtil {

    public static boolean match(String regex, String text) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        boolean isMatch = m.matches();
        return isMatch;
    }

}
