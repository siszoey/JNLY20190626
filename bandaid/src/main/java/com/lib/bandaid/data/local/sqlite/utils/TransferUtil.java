package com.lib.bandaid.data.local.sqlite.utils;

import com.lib.bandaid.data.local.sqlite.core.annotation.Column;
import com.lib.bandaid.data.local.sqlite.core.annotation.Format;
import com.lib.bandaid.data.local.sqlite.core.bean.KeyValue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zy on 2017/1/23.
 * 基本类型转换
 */

public final class TransferUtil {
    private TransferUtil() {

    }

    public static String objectToSqlKeyValStr(Class<?> type, String columnName, Object value) {
        StringBuilder values = new StringBuilder();
        if (type == Integer.class) {
            values.append(columnName);
            values.append(" = ");
            values.append(value);
        } else if (type == Double.class) {
            values.append(columnName);
            values.append(" = ");
            values.append(value);
        } else if (type == Float.class) {
            values.append(columnName);
            values.append(" = ");
            values.append(value);
        } else if (type == Long.class) {
            values.append(columnName);
            values.append(" = ");
            values.append(value);
        } else if (type == Short.class) {
            values.append(columnName);
            values.append(" = ");
            values.append(value);
        } else if (type == String.class) {
            if (value == null || value.equals("null")) {
                values.append(columnName);
                values.append(" = ");
                values.append(value);
            } else {
                values.append(columnName);
                values.append(" = '");
                values.append(value);
                values.append("' ");
            }
        } else if (type == Date.class) {
            values.append(columnName);
            values.append(" = '");
            values.append(value);
            values.append("' ");
        } else {
            values.append(columnName);
            values.append(" = '");
            values.append(value);
            values.append("' ");
        }
        return values.toString();
    }

    public static String objectToSqlValStr(Class<?> type, Object value) {
        StringBuilder values = new StringBuilder();
        if (value != null) {
            if (type == Integer.class) {
                values.append(value);
            } else if (type == Double.class) {
                values.append(value);
            } else if (type == Float.class) {
                values.append(value);
            } else if (type == Long.class) {
                values.append(value);
            } else if (type == Short.class) {
                values.append(value);
            } else if (type == String.class) {
                values.append("'").append(value).append("'");
            } else if (type == Date.class) {
                values.append("'").append(value).append("'");
            } else {
                values.append("'").append(value).append("'");
            }
        } else {
            values.append("NULL");
        }
        return values.toString();
    }

    /**
     * 获取实体的 save  key1，key2，key3....
     *
     * @return
     */
    public static String getTKeysSql(Object obj) {
        Class<? extends Object> clazz = obj.getClass();
        List<Field> fields = ReflectUtil.getIterationFields(clazz);
        Field field;
        String columnName;
        StringBuilder keyBuilder = new StringBuilder();
        for (int i = 0; i < fields.size(); i++) {
            field = fields.get(i);
            if (field.isAnnotationPresent(Column.class)) {
                columnName = ReflectUtil.getColumnName(field);
                keyBuilder.append(columnName).append(",");
            }
        }
        if (keyBuilder.toString().endsWith(",")) {
            int endFields = keyBuilder.lastIndexOf(",");
            keyBuilder.delete(endFields, endFields + 1);
            return keyBuilder.toString();
        } else {
            return null;
        }
    }

    /**
     * 获取实体的 save  val1，val2，val3....
     *
     * @return
     */
    public static String getTValuesSql(Object obj) {
        Class<? extends Object> clazz = obj.getClass();
        List<Field> fields = ReflectUtil.getIterationFields(clazz);
        StringBuilder valBuilder = new StringBuilder();
        Field field;
        String columnName;
        Format format;
        Class<?> type;
        Method getMethod;
        Object value;
        String valStr;
        try {
            for (int i = 0; i < fields.size(); i++) {
                field = fields.get(i);
                if (field.isAnnotationPresent(Column.class)) {
                    type = field.getType();
                    getMethod = ReflectUtil.getMethod(clazz, field);
                    if (null != getMethod) {
                        value = getMethod.invoke(obj, new Object[]{});
                    } else {
                        field.setAccessible(true);
                        value = field.get(obj);
                    }
                    if (type == Date.class) {
                        if (getMethod.isAnnotationPresent(Format.class)) { //如果指定了 日期的转换格式则执行转换
                            format = getMethod.getAnnotation(Format.class);
                            String pattern = format.dateTimePattern();
                            value = DateUtil.getDAT(pattern, (Date) value);
                        } else {
                            value = DateUtil.getDAT("yyyy-MM-dd HH:mm:ss", (Date) value);
                        }
                    }
                    valStr = objectToSqlValStr(type, value);
                    valBuilder.append(valStr).append(",");
                }
            }
            if (valBuilder.toString().endsWith(",")) {
                int endFields = valBuilder.lastIndexOf(",");
                valBuilder.delete(endFields, endFields + 1);
                return valBuilder.toString();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }


    public static Map<String, String> getTKVSql(Object obj) {
        Class<? extends Object> clazz = obj.getClass();
        List<Field> fields = ReflectUtil.getIterationFields(clazz);
        Map<String, String> map = new HashMap<>();
        StringBuilder keyBuilder = new StringBuilder();
        StringBuilder valBuilder = new StringBuilder();
        Field field;
        String columnName;
        Format format;
        Class<?> type;
        Method getMethod;
        Object value;
        String valStr;
        try {
            for (int i = 0; i < fields.size(); i++) {
                field = fields.get(i);
                if (field.isAnnotationPresent(Column.class)) {
                    columnName = ReflectUtil.getColumnName(field);
                    keyBuilder.append(columnName).append(",");
                    type = field.getType();
                    getMethod = ReflectUtil.getMethod(clazz, field);
                    if (null != getMethod) {
                        value = getMethod.invoke(obj, new Object[]{});
                    } else {
                        field.setAccessible(true);
                        value = field.get(obj);
                    }
                    if (type == Date.class) {
                        if (getMethod.isAnnotationPresent(Format.class)) { //如果指定了 日期的转换格式则执行转换
                            format = getMethod.getAnnotation(Format.class);
                            String pattern = format.dateTimePattern();
                            value = DateUtil.getDAT(pattern, (Date) value);
                        } else {
                            value = DateUtil.getDAT("yyyy-MM-dd HH:mm:ss", (Date) value);
                        }
                    }
                    valStr = objectToSqlValStr(type, value);
                    valBuilder.append(valStr).append(",");
                }
            }
            if (keyBuilder.toString().endsWith(",")) {
                int endFields = keyBuilder.lastIndexOf(",");
                keyBuilder.delete(endFields, endFields + 1);
            }
            if (valBuilder.toString().endsWith(",")) {
                int endFields = valBuilder.lastIndexOf(",");
                valBuilder.delete(endFields, endFields + 1);
            }
            map.put("Key", keyBuilder.toString());
            map.put("Value", valBuilder.toString());
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Object string2SimpleObject(Field field, String s) {
        Object o = null;
        Type type = field.getType();
        try {
            if (null == s) {
                return null;
            } else if ("".equals(s)) {
                return null;
            } else if (type == Integer.class || type == int.class) {
                o = Integer.parseInt(s);
            } else if (type == Float.class || type == float.class) {
                o = Float.parseFloat(s);
            } else if (type == Double.class || type == double.class) {
                o = Double.parseDouble(s);
            } else if (type == Long.class || type == long.class) {
                o = Long.parseLong(s);
            } else if (type == Short.class || type == short.class) {
                o = Short.parseShort(s);
            } else if (type == String.class) {
                o = s;
            } else if (type == Date.class) {
                o = DateUtil.strToDateTime(s);
            }
        } catch (Exception e) {
            o = null;
            e.printStackTrace();
        }
        return o;
    }


    public static String object2String(Field field, Object obj) {
        String s = null;
        Type type = field.getType();
        try {
            if (null == obj) {
                return "";
            } else if (type == Integer.class || type == int.class) {
                s = ((Integer) obj).toString();
            } else if (type == Float.class || type == float.class) {
                s = ((Float) obj).toString();
            } else if (type == Double.class || type == double.class) {
                s = ((Double) obj).toString();
            } else if (type == Long.class || type == long.class) {
                s = ((Long) obj).toString();
            } else if (type == Short.class || type == short.class) {
                s = ((Short) obj).toString();
            } else if (type == String.class) {
                s = obj.toString();
            } else if (type == Date.class) {
                s = DateUtil.dateTimeToStr((Date) obj);
            }
        } catch (Exception e) {
            s = "";
            e.printStackTrace();
        }
        return s;
    }


    /**
     * 实体转map
     *
     * @param obj
     * @return
     */
    public static Map<String, String> entity2Map(Object obj) {
        Class<? extends Object> clazz = obj.getClass();
        List<Field> fields = ReflectUtil.getIterationFields(clazz);
        Map<String, String> map = new HashMap<>();
        try {
            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                String key = field.getName();
                String getMethodName = ReflectUtil.getMethodName(field);
                Method getMethod = clazz.getMethod(getMethodName, new Class[]{});
                Object value = getMethod.invoke(obj, new Object[]{});//获取属性值
                String _value = TransferUtil.object2String(field, value);
                map.put(key, _value);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map = null;
        }
        return map;
    }

    /**
     * 实体转map
     *
     * @param obj
     * @return
     */
    public static Map<String, String> entity2MapWithAliasOrder(Object obj) {
        Class<? extends Object> clazz = obj.getClass();
        String[] columnArray = ReflectUtil.columnArray(clazz);
        Map<String, String> map = new LinkedHashMap<>();
        try {
            for (int i = 0; i < columnArray.length; i++) {
                String columnName = columnArray[i];
                Field field = clazz.getDeclaredField(columnName);
                String key = ReflectUtil.getColumnAlias(field);
                String getMethodName = ReflectUtil.getMethodName(field);
                Method getMethod = clazz.getMethod(getMethodName, new Class[]{});
                Object value = getMethod.invoke(obj, new Object[]{});//获取属性值
                String _value = TransferUtil.object2String(field, value);
                map.put(key, _value);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map = null;
        }
        return map;
    }

    /**
     * 实体转map
     *
     * @param obj
     * @return
     */
    public static Map<String, String> entity2MapWithAlias(Object obj) {
        Class<? extends Object> clazz = obj.getClass();
        List<Field> fields = ReflectUtil.getIterationFields(clazz);
        Map<String, String> map = new LinkedHashMap<>();
        try {
            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                String key = ReflectUtil.getColumnAlias(field);
                String getMethodName = ReflectUtil.getMethodName(field);
                Method getMethod = clazz.getMethod(getMethodName, new Class[]{});
                Object value = getMethod.invoke(obj, new Object[]{});//获取属性值
                String _value = TransferUtil.object2String(field, value);
                map.put(key, _value);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map = null;
        }
        return map;
    }

    public static List<KeyValue> Map2KeyValueList(Map<String, Object> map) {
        List<KeyValue> list = null;
        KeyValue keyValue;
        if (map != null && map.size() > 0) {
            list = new ArrayList<>();
            for (String key : map.keySet()) {
                keyValue = new KeyValue();
                keyValue.setKey(key);
                keyValue.setValue(map.get(key));
                list.add(keyValue);
            }
        }
        return list;
    }

    public static List<KeyValue> entity2KeyValueList(Object obj) {
        Map map = entity2MapWithAlias(obj);
        return Map2KeyValueList(map);
    }

    public static List<KeyValue> entity2KeyValueListOrder(Object obj) {
        Map map = entity2MapWithAliasOrder(obj);
        return Map2KeyValueList(map);
    }

    /**
     * 更具实体和指定字段转map
     *
     * @param obj   实体
     * @param array 数据库列名
     * @return
     */
    public static Map<String, Object> T2Map(Object obj, String[] array) {
        Map<String, Object> map = null;
        try {
            map = new HashMap<>();
            Class<? extends Object> clazz = obj.getClass();
            //Field[] fields = clazz.getDeclaredFields();
            List<Field> fields = ReflectUtil.getIterationFields(clazz);
            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                String columnName = ReflectUtil.getColumnName(field);
                for (int j = 0; j < array.length; j++) {
                    String name = array[j];
                    if (columnName.equals(name)) {
                        String getMethodName = ReflectUtil.getMethodName(field);
                        Method getMethod = clazz.getMethod(getMethodName, new Class[]{});
                        Object value = getMethod.invoke(obj, new Object[]{});//获取属性值
                        map.put(name, value);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            map = null;
        }
        return map;
    }

    public static String objectToInWhere(Class<?> type, Object value) {
        StringBuilder where = new StringBuilder();
        if (type == Integer.class) {
            where.append(value);
            where.append(",");
        } else if (type == Double.class) {
            where.append(value);
            where.append(",");
        } else if (type == Float.class) {
            where.append(value);
            where.append(",");
        } else if (type == Long.class) {
            where.append(value);
            where.append(",");
        } else if (type == Short.class) {
            where.append(value);
            where.append(",");
        } else if (type == String.class) {
            where.append("'");
            where.append(value);
            where.append("',");
        } else if (type == Date.class) {
            where.append("'");
            where.append(value);
            where.append("',");
        } else {
            where.append("'");
            where.append(value);
            where.append("',");
        }
        return where.toString();
    }

}
