package com.lib.bandaid.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

public final class PyUtil {

    private PyUtil(){}

    /**
     * 将字符串中的中文转化为拼音,其他字符不变
     *
     * @param inputString
     * @return
     */
    private String convert2PingYin(String inputString) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        char[] input = inputString.trim().toCharArray();// 把字符串转化成字符数组
        String output = "";
        String[] temp;
        try {
            for (int i = 0; i < input.length; i++) {
                char item = input[i];
                // \\u4E00是unicode编码，判断是不是中文
                boolean isChinese = false;
                if (java.lang.Character.toString(item).matches("[\\u4E00-\\u9FA5]+")) {
                    isChinese = true;
                    // 将汉语拼音的全拼存到temp数组
                    temp = PinyinHelper.toHanyuPinyinStringArray(item, format);
                    // 取拼音的第一个读音
                    output += temp[0];
                }
                // 大写字母转化成小写字母
                else if (item > 'A' && item < 'Z') {
                    output += java.lang.Character.toString(item);
                    output = output.toLowerCase();
                }
                if (isChinese) continue;
                output += java.lang.Character.toString(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

}

