package com.titan.jnly.common.uitl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 富文本处理
 */
public final class RichTxtUtil {

    public static List<String> getRichTxtImgUrl(String richTxt) {
        List<String> list = new ArrayList<>();
        String img = "";
        Pattern p_image;
        Matcher m_image;
        // String regEx_img = "<img.*src=(.*?)[^>]*?>"; //图片链接地址
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(richTxt);
        Matcher m;
        while (m_image.find()) {
            // 得到<img />数据
            img = m_image.group();
            // 匹配<img>中的src数据
            m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                list.add(m.group(1));
            }
        }
        return list;
    }

    /**
     * 往富文本里的img标签的src里追加url
     *
     * @param richTxt
     * @return
     */
    public static String appendRichTxtImgUrl(String richTxt, String prefix) {
        List<String> list = getRichTxtImgUrl(richTxt);
        Map<String, Object> temp = new HashMap<>();
        for (String s : list) {
            temp.put(s, null);
        }
        list.clear();
        for (String key : temp.keySet()) {
            list.add(key);
        }
        for (String url : list) {
            richTxt = richTxt.replace(url, prefix + url);
        }
        return richTxt;
    }


    public static void main(String[] args) {
        String richTxt = "如附图附图图<img src=\"/attached/image/20191220/20191220144525_9277.jpg\" alt=\"\" /><img src=\"/attached/image/20191220/20191220144525_9277.jpg\" alt=\"\" />";
        richTxt = appendRichTxtImgUrl(richTxt, "http://");
        System.out.println(richTxt);
    }

}
