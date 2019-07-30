package com.lib.bandaid.widget.easyui.utils;

import com.lib.bandaid.widget.easyui.xml.EasyUiXml;
import com.lib.bandaid.widget.easyui.xml.ItemXml;
import com.lib.bandaid.widget.easyui.xml.UiXml;

import java.util.ArrayList;
import java.util.List;

public final class EasyUtil {

    public final static String SPLIT = ";";

    public static String list2Label(List<ItemXml> values) {
        if (values == null) return null;
        StringBuilder label = new StringBuilder();
        ItemXml value;
        for (int i = 0; i < values.size(); i++) {
            value = values.get(i);
            label.append(value.getValue());
            if (i != values.size() - 1) label.append(SPLIT);
        }
        return label.toString();
    }

    public static List<Integer> label2Index(List<ItemXml> list, String labels) {
        if (labels == null) return null;
        String[] array = labels.split(SPLIT);
        if (array == null) return null;
        List<Integer> res = new ArrayList<>();
        ItemXml value;
        for (String label : array) {
            if (label == null) continue;
            for (int j = 0; j < list.size(); j++) {
                value = list.get(j);
                if (value == null) continue;
                if (label.equals(value.getValue())) {
                    if (!res.contains(j)) res.add(j);
                }
            }
        }
        return res;
    }

    public static List<Integer> label2Index(UiXml uiXml, String labels) {
        if (labels == null) return null;
        String[] array = labels.split(SPLIT);
        if (array == null) return null;
        List<Integer> res = new ArrayList<>();
        List<ItemXml> list = uiXml.getItemXml();
        ItemXml value;
        for (String label : array) {
            if (label == null) continue;
            for (int j = 0; j < list.size(); j++) {
                value = list.get(j);
                if (value == null) continue;
                if (label.equals(value.getValue())) {
                    if (!res.contains(j)) res.add(j);
                }
            }
        }
        return res;
    }

    public static List<ItemXml> label2List(UiXml uiXml, String labels) {
        List<Integer> index = label2Index(uiXml, labels);
        if (index == null) return null;
        List<ItemXml> list = uiXml.getItemXml();
        if (list == null) return null;
        List<ItemXml> res = new ArrayList<>();
        for (Integer i : index) {
            res.add(list.get(i));
        }
        return res;
    }

    public static String code2Labels(UiXml uiXml, String codes) {
        if (codes == null) return null;
        String[] array = codes.split(SPLIT);
        if (array == null || array.length == 0) return null;
        boolean canConcat = canConcat(uiXml);
        if (canConcat) {
            ItemXml item;
            String label;
            String code;
            List<ItemXml> list = uiXml.getItemXml();
            List<String> res = new ArrayList<>();
            for (String s : array) {
                for (int j = 0; j < list.size(); j++) {
                    item = list.get(j);
                    label = item.getValue();
                    code = item.getCode() + "";
                    if (s == null) continue;
                    if (s.equals(code) && !res.contains(label)) {
                        res.add(label);
                    }
                }
            }
            if (res.size() == 0) return null;
            StringBuilder sb = new StringBuilder();
            for (int i = 0, len = res.size(); i < len; i++) {
                sb.append(res.get(i));
                if (i != len - 1) sb.append(SPLIT);
            }
            return sb.toString();
        }
        return null;
    }

    public static String label2Codes(UiXml uiXml, String labels) {
        if (labels == null) return null;
        String[] array = labels.split(SPLIT);
        if (array == null || array.length == 0) return null;
        boolean canConcat = canConcat(uiXml);
        if (canConcat) {
            List<ItemXml> list = uiXml.getItemXml();
            ItemXml item;
            String value;
            String code;
            List<String> res = new ArrayList<>();
            for (String label : array) {
                for (int j = 0; j < list.size(); j++) {
                    item = list.get(j);
                    value = item.getValue();
                    code = item.getCode() + "";
                    if (value == null) continue;
                    if (value.equals(label) && !res.contains(code)) {
                        res.add(code);
                    }
                }
            }
            if (res.size() == 0) return null;
            StringBuilder sb = new StringBuilder();
            for (int i = 0, len = res.size(); i < len; i++) {
                sb.append(res.get(i));
                if (i != len - 1) sb.append(SPLIT);
            }
            return sb.toString();
        }
        return null;
    }

    /**
     * @param uiXml
     * @return
     */
    public static boolean canConcat(UiXml uiXml) {
        boolean dbCanSave = uiXml.getDataType() == String.class;
        if (!dbCanSave) return false;
        List<ItemXml> list = uiXml.getItemXml();
        if (list == null || list.size() == 0) return false;
        ItemXml item = list.get(0);
        Object codeForSave = item.getCode();
        if (codeForSave == null) return false;
        if (codeForSave instanceof String) return true;
        return false;
    }

    public static UiXml findUiXmlByKey(EasyUiXml easyUiXml, String key) {
        List<UiXml> list = easyUiXml.getUiXml();
        if (list == null) return null;
        for (UiXml uiXml : list) {
            if (uiXml == null) continue;
            if (uiXml.getCode().equals(key)) return uiXml;
        }
        return null;
    }

    public static UiXml findUiXmlByAlias(EasyUiXml easyUiXml, String alias) {
        List<UiXml> list = easyUiXml.getUiXml();
        if (list == null) return null;
        for (UiXml uiXml : list) {
            if (uiXml == null) continue;
            if (uiXml.getAlias().equals(alias)) return uiXml;
        }
        return null;
    }
}
