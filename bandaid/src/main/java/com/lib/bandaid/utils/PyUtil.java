package com.lib.bandaid.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

public final class PyUtil {

    private static PyUtil instance;

    private HanyuPinyinOutputFormat format;

    private PyUtil() {
        format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
    }

    //方法同步，调用效率低
    public static synchronized PyUtil getInstance() {
        if (instance == null) {
            instance = new PyUtil();
        }
        return instance;
    }


    /**
     * 将字符串中的中文转化为拼音,其他字符不变
     *
     * @param inputString
     * @return
     */
    public String convert2PingYin(String inputString) {
        char[] input = inputString.trim().toCharArray();// 把字符串转化成字符数组
        String output = "";
        String[] temp;
        try {
            char item;
            boolean isChinese;
            for (int i = 0, len = input.length; i < len; i++) {
                item = input[i];
                isChinese = isChinese(item);
                if (isChinese) {
                    temp = PinyinHelper.toHanyuPinyinStringArray(item, format);
                    output += temp[0];
                } else if (item > 'A' && item < 'Z') {
                    output += Character.toString(item);
                    output = output.toLowerCase();
                }
                if (isChinese) continue;
                output += Character.toString(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    /**
     * 将字符串中的中文转化为拼音,其他字符不变
     *
     * @param inputString
     * @return
     */
    public String convert2Py(String inputString) {
        char[] input = inputString.trim().toCharArray();// 把字符串转化成字符数组
        StringBuilder output = new StringBuilder();
        String[] temp;
        try {
            char item;
            boolean isChinese;
            for (int i = 0, len = input.length; i < len; i++) {
                item = input[i];
                isChinese = isChinese(item);
                if (isChinese) {
                    temp = PinyinHelper.toHanyuPinyinStringArray(item, format);
                    output.append(temp[0]);
                }
                if (isChinese) continue;
                output.append(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output.toString();
    }


    public String convert2PyHead(String inputString) {
        char[] input = inputString.trim().toCharArray();// 把字符串转化成字符数组
        StringBuilder output = new StringBuilder();
        String[] temp;
        try {
            char item;
            boolean isChinese;
            for (int i = 0, len = input.length; i < len; i++) {
                item = input[i];
                isChinese = isChinese(item);
                if (isChinese) {
                    temp = PinyinHelper.toHanyuPinyinStringArray(item, format);
                    output.append(temp[0].toCharArray()[0]);
                }
                if (isChinese) continue;
                output.append(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output.toString();
    }

    public static boolean isChinese(char item) {
        return Character.toString(item).matches("[\\u4E00-\\u9FA5]+");
    }

}

