package com.lib.bandaid.widget.easyui.convert;

import com.esri.arcgisruntime.data.CodedValue;
import com.esri.arcgisruntime.data.CodedValueDomain;
import com.esri.arcgisruntime.data.Domain;
import com.esri.arcgisruntime.data.Field;

import com.lib.bandaid.widget.easyui.xml.EasyUiXml;
import com.lib.bandaid.widget.easyui.xml.ItemXml;
import com.lib.bandaid.widget.easyui.xml.UiXml;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

/**
 * 解析
 */
public final class Resolution {

    public static EasyUiXml convert2EasyUiXml(EasyUiXml easyUiXml, Map<String, Object> property) {
        if (easyUiXml == null) return null;
        UiXml uiXml;
        List<UiXml> list = easyUiXml.getUiXml();
        for (int i = 0, len = list.size(); i < len; i++) {
            uiXml = list.get(i);
            if (uiXml == null) continue;
            for (String key : property.keySet()) {
                if (uiXml.getCode().equals(key)) {
                    uiXml.setValue(property.get(key));
                }
            }
        }
        return easyUiXml;
    }

    public static EasyUiXml convert2EasyUiXml(List<Field> fields, Map<String, Object> property) {
        EasyUiXml easyUiXml = convert2EasyUiXml(fields);
        if (easyUiXml == null) return null;
        UiXml uiXml;
        List<UiXml> list = easyUiXml.getUiXml();
        for (int i = 0, len = list.size(); i < len; i++) {
            uiXml = list.get(i);
            if (uiXml == null) continue;
            for (String key : property.keySet()) {
                if (uiXml.getCode().equals(key)) {
                    uiXml.setValue(property.get(key));
                }
            }
        }
        return easyUiXml;
    }

    public static EasyUiXml convert2EasyUiXml(List<Field> fields) {
        EasyUiXml easyUiXml = EasyUiXml.create();
        if (fields == null) return easyUiXml;
        Field field;
        UiXml uiXml;
        for (int i = 0, len = fields.size(); i < len; i++) {
            field = fields.get(i);
            uiXml = convert2UiXml(field);
            if (uiXml == null) continue;
            easyUiXml.addUiXml(uiXml);
        }
        return easyUiXml;
    }

    public static UiXml convert2UiXml(Field field) {
        if (field == null) return null;
        Domain domain = field.getDomain();
        CodedValueDomain codedValueDomain;
        UiXml uiXml = new UiXml();
        uiXml.setCode(field.getName());
        uiXml.setAlias(field.getAlias());
        uiXml.setReadonly(!field.isEditable());
        Type type = convertJavaFieldType(field);
        uiXml.setDataType(type);
        if (domain != null && domain instanceof CodedValueDomain) {
            codedValueDomain = (CodedValueDomain) domain;
            if (codedValueDomain != null) {
                List<ItemXml> list = convert2UiXml(codedValueDomain);
                uiXml.addItemXml(list);
            }
        }
        return uiXml;
    }

    public static List<ItemXml> convert2UiXml(CodedValueDomain domain) {
        if (domain == null) return null;
        List<CodedValue> codedValues = domain.getCodedValues();
        if (codedValues == null) return null;
        ItemXml itemXml;
        List<ItemXml> list = new ArrayList<>();
        for (CodedValue value : codedValues) {
            itemXml = new ItemXml();
            itemXml.setCode(value.getCode());
            itemXml.setValue(value.getName());
            list.add(itemXml);
        }
        return list;
    }

    /**
     * 将arcgis runtime类型转成java的数据类型
     *
     * @param field
     * @return
     */
    public static Type convertJavaFieldType(Field field) {
        Field.Type runTimeType = field.getFieldType();
        if (runTimeType == Field.Type.UNKNOWN) {
            return String.class;
        } else if (runTimeType == Field.Type.SHORT) {
            return Short.class;
        } else if (runTimeType == Field.Type.INTEGER) {
            return Integer.class;
        } else if (runTimeType == Field.Type.GUID) {
            return String.class;
        } else if (runTimeType == Field.Type.FLOAT) {
            return Float.class;
        } else if (runTimeType == Field.Type.SHORT) {
            return Short.class;
        } else if (runTimeType == Field.Type.DOUBLE) {
            return Double.class;
        } else if (runTimeType == Field.Type.DATE) {
            return GregorianCalendar.class;
        } else if (runTimeType == Field.Type.TEXT) {
            return String.class;
        } else if (runTimeType == Field.Type.OID) {
            return Long.class;
        } else if (runTimeType == Field.Type.GLOBALID) {
            return String.class;
        } else if (runTimeType == Field.Type.BLOB) {
            return byte[].class;
        } else if (runTimeType == Field.Type.GEOMETRY) {
            return String.class;
        } else if (runTimeType == Field.Type.RASTER) {
            return byte[].class;
        } else if (runTimeType == Field.Type.XML) {
            return String.class;
        } else {
            return String.class;
        }
    }

}
