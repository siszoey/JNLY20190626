package com.lib.bandaid.data.local.sqlite.utils;

import com.lib.bandaid.data.local.sqlite.core.annotation.Column;
import com.lib.bandaid.data.local.sqlite.core.annotation.Table;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zy on 2017/1/20.
 * 反射帮助
 */

public final class ReflectUtil {

    private ReflectUtil() {
    }


    public static List<Field> getIterationFields(Class clazz) {
        List<Field> fields = new ArrayList<>();
        Class tempClass = clazz;
        while (tempClass != null && !tempClass.getName().toLowerCase().equals("java.lang.object")) {
            fields.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            tempClass = tempClass.getSuperclass();
        }
        return fields;
    }


    /**
     * 获取get方法名称
     *
     * @param field
     * @return
     */
    public static String getMethodName(Field field) {
        String fieldName = field.getName();
        String firstLetter = fieldName.substring(0, 1).toUpperCase(); // 获得和属性对应的getXXX()方法的名字
        String getMethodName = "get" + firstLetter + fieldName.substring(1); // 获得和属性对应的getXXX()方法的名字
        return getMethodName;
    }

    /**
     * 获取set方法名称
     *
     * @param field
     * @return
     */
    public static String setMethodName(Field field) {
        String fieldName = field.getName();
        String firstLetter = fieldName.substring(0, 1).toUpperCase(); // 获得和属性对应的getXXX()方法的名字
        String setMethodName = "set" + firstLetter + fieldName.substring(1); // 获得和属性对应的getXXX()方法的名字
        return setMethodName;
    }

    /**
     * 获取get方法名称
     *
     * @param field
     * @return
     */
    public static String getMethod(Field field) {
        String fieldName = field.getName();
        String firstLetter = fieldName.substring(0, 1).toUpperCase(); // 获得和属性对应的getXXX()方法的名字
        String getMethodName = "get" + firstLetter + fieldName.substring(1); // 获得和属性对应的getXXX()方法的名字
        return getMethodName;
    }

    /**
     * 获取set方法名称
     *
     * @param field
     * @return
     */
    public static Method setMethod(Class<?> clazz, Field field) {
        Method setMethod = null;
        try {
            String setMethodName = setMethodName(field);
            Class[] parameterTypes = new Class[1];
            parameterTypes[0] = field.getType();
            setMethod = clazz.getMethod(setMethodName, parameterTypes);
        } catch (Exception e) {
//            e.printStackTrace();
            setMethod = null;
        }
        return setMethod;
    }

    public static Method getMethod(Class<?> clazz, Field field) {
        Method getMethod = null;
        try {
            String getMethodName = getMethodName(field);
            getMethod = clazz.getMethod(getMethodName, new Class[]{});
        } catch (Exception e) {
            e.printStackTrace();
            getMethod = null;
        }
        return getMethod;
    }

    /**
     * 通过反射获取表名
     *
     * @param clazz
     * @return
     */
    public static String getTableName(Class<?> clazz) {
        Table table = clazz.getAnnotation(Table.class);
        String tableName = table.name();
        if (tableName.equals("default")) {
            tableName = clazz.getSimpleName();
        }
        return tableName;
    }

    /**
     * 给类，和属性 获取 列名
     *
     * @param clazz
     * @param attributeName
     * @return
     */
    public static String getColumnName(Class<?> clazz, String attributeName) {
        try {
            String simpleClassName = clazz.getSimpleName();
            Field field = clazz.getDeclaredField(attributeName);
            if (field != null) {
                return getColumnName(field);
            } else {
                return attributeName;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return attributeName;
        }
    }

    public static String getColumnName(Field field) {
        Column column = field.getAnnotation(Column.class);
        if (column != null) {
            String columnName = column.name();//列名(自定义的列名称)
            if (columnName.equals("default")) {
                columnName = field.getName();//默认的列名称
            }//获取列名
            return columnName;
        } else {
            return field.getName();
        }
    }

    public static String getColumnAlias(Field field) {
        Column column = field.getAnnotation(Column.class);
        if (column != null) {
            String columnAlias = column.alias();//列名(自定义的列名称)
            if (columnAlias.equals("default")) {
                columnAlias = field.getName();//默认的列名称
            }//获取列名
            return columnAlias;
        } else {
            return field.getName();
        }
    }

    /**
     * 通过反射获取表名  FName as name,FName as name,FName as name
     *
     * @param clazz
     * @return
     */
    public static String getNameAsAlias(Class<?> clazz) {
        StringBuilder stringBuilder = new StringBuilder();
        List<Field> fields = ReflectUtil.getIterationFields(clazz);
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            if (field.isAnnotationPresent(Column.class)) {
                String columnName = queryFun(field);
                String name = field.getName();
                stringBuilder.append(columnName).append(" as ").append(name);
                if (i != fields.size() - 1) {
                    stringBuilder.append(",");
                }
            }
        }
        return stringBuilder.toString();
    }

    public static Type getColumnType(Field field) {
        return null;
    }

    /**
     * 通过反射获取主键名称
     *
     * @param clazz
     * @return
     */
    public static String getPrimaryKeyName(Class<?> clazz) {
        String columnName = null;
        List<Field> fields = ReflectUtil.getIterationFields(clazz);
        for (int i = 0; i < fields.size(); i++) {
            Field f = fields.get(i);
            if (f.isAnnotationPresent(Column.class)) {
                Column column = f.getAnnotation(Column.class);
                boolean columnPKey = column.isPKey();//主键
                if (columnPKey) {
                    columnName = column.name();//列名
                    if (columnName.equals("default")) {
                        columnName = f.getName();
                    }
                    break;
                } else {
                    continue;
                }
            }
        }
        return columnName;
    }

    public static boolean isPKey(Field field) {
        Column column = field.getAnnotation(Column.class);
        Boolean columnPKey = column.isPKey();//主键
        return columnPKey;
    }

    /**
     * 通过反射获取主键值(单一主键)
     *
     * @param obj
     * @return
     */
    public static Object getPrimaryKeyValue(Object obj) {
        Object value = null;
        try {
            Class<? extends Object> clazz = obj.getClass();
            List<Field> fields = ReflectUtil.getIterationFields(clazz);
            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                if (field.isAnnotationPresent(Column.class)) {
                    Column column = field.getAnnotation(Column.class);
                    Boolean columnPKey = column.isPKey();//主键
                    if (columnPKey) {
                        String getMethodName = getMethodName(field);
                        Method getMethod = clazz.getMethod(getMethodName, new Class[]{});
                        value = getMethod.invoke(obj, new Object[]{});//获取属性值
                        break;
                    }
                } else {
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 通过反射获取自定义注解 主键kv;(支持复合主键)
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> getPrimaryMap(Object obj) {
        Map map = new HashMap();
        try {
            Object value = null;
            Class<? extends Object> clazz = obj.getClass();
            List<Field> fields = ReflectUtil.getIterationFields(clazz);
            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                if (field.isAnnotationPresent(Column.class)) {
                    Column column = field.getAnnotation(Column.class);
                    String fieldName = ReflectUtil.getColumnName(field);
                    Boolean columnPKey = column.isPKey();//主键
                    if (columnPKey) {
                        String getMethodName = getMethodName(field);
                        Method getMethod = clazz.getMethod(getMethodName, new Class[]{});
                        value = getMethod.invoke(obj, new Object[]{});//获取属性值
                        map.put(fieldName, value);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 通过反射获取自定义注解 主键kv;(支持复合主键)
     *
     * @param list
     * @return
     */
    public static List<Map<String, Object>> getPrimaryList(List list) {
        List<Map<String, Object>> listMap = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Map map = getPrimaryMap(list.get(i));
            listMap.add(map);
        }
        return listMap;
    }

    /**
     * @param clazz
     * @return
     */
    public static String[] columnArray(Class clazz) {
        try {
            Object obj = clazz.newInstance();
            String s = obj.toString();
            String className = obj.getClass().getSimpleName();
            s = s.substring(className.length());
            s = s.replaceAll("'", "").replaceAll(" ", "").replaceAll("[{]", "").replaceAll("[}]", "").replaceAll("[\\[]", "").replaceAll("[\\]]", "");
            String[] columnArray = s.split(",");
            for (int i = 0; i < columnArray.length; i++) {
                String _column = columnArray[i];
                _column = _column.substring(0, _column.indexOf("="));
                columnArray[i] = _column;
            }
            return columnArray;
        } catch (Exception e) {
            return null;
        }
    }

    public static Field getFileByAliasName(Class clazz, String aliasName) {
        try {
            Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                if (field.isAnnotationPresent(Column.class)) {
                    String _aliasName = getColumnAlias(field);
                    if (_aliasName.equals(aliasName)) {
                        return field;
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public static Field getFileByColumnName(Class clazz, String columnName) {
        try {
            Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                if (field.isAnnotationPresent(Column.class)) {
                    String _columnName = getColumnName(field);
                    if (_columnName.equals(columnName)) {
                        return field;
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * 特殊字段的函数处理
     *
     * @return
     */
    public static String saveFun(Field field, String value) {
        /**
         * 特殊函数处理
         */
        Column column = field.getAnnotation(Column.class);
        String function = column.save();
        if (!function.equals("default")) {
            value = value.substring(0, value.length() - 1);
            value = function.replace("?", value) + ",";
        }
        return value;
    }

    /**
     * 特殊字段的函数处理
     *
     * @return
     */
    public static String updateFun(Field field, String value) {
        /**
         * 特殊函数处理
         */
        Column column = field.getAnnotation(Column.class);
        String function = column.update();
        if (!function.equals("default")) {
            value = value.substring(0, value.length() - 1);
            value = function.replace("?", value) + ",";
        }
        return value;
    }

    /**
     * 特殊字段的函数处理
     *
     * @return
     */
    public static String queryFun(Field field, String value) {
        /**
         * 特殊函数处理
         */
        Column column = field.getAnnotation(Column.class);
        String function = column.query();
        if (!function.equals("default")) {
            value = function.replace("?", value);
        }
        return value;
    }

    /**
     * 特殊字段的函数处理
     *
     * @return
     */
    public static String queryFun(Field field) {
        /**
         * 特殊函数处理
         */
        Column column = field.getAnnotation(Column.class);
        String value = getColumnName(field);
        String function = column.query();
        if (!function.equals("default")) {
            value = function.replace("?", value);
        }
        return value;
    }


    public static String map2SqlSimpleWhere(Map<String, Object> condition) {
        StringBuilder where = null;
        if (condition != null && condition.size() > 0) {
            where = new StringBuilder();
            int count = 0;
            int sum = condition.size();
            for (String key : condition.keySet()) {
                count++;
                String k = key;
                Object v = condition.get(k);
                if (v == null) {
                    where.append(k).append(" = null");
                    if (count != sum) {
                        where.append(" and ");
                    }
                } else {

                    if (v instanceof String) {
                        where.append(k).append(" = '").append(v.toString()).append("'");
                    } else {
                        where.append(k).append(" = ").append(v.toString());
                    }

                    if (count != sum) {
                        where.append(" and ");
                    }
                }
            }
            return where.toString();
        }
        return null;
    }


    public static Class<?> getFieldTypeByName(Class clazz, String name) {
        if (clazz == null || name == null) {
            return null;
        }
        try {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().equals(name)) {
                    return field.getType();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Field getFieldByName(Class clazz, String name) {
        if (clazz == null || name == null) {
            return null;
        }
        try {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().equals(name)) {
                    return field;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param obj
     * @param name
     * @param <T>
     * @return
     */
    public static <T> T getValByNumberVar(Object obj, String name) {
        if (obj == null) {
            return null;
        }
        try {
            Field field = getFieldByName(obj.getClass(), name);
            if (field == null) return null;
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) return null;
            field.setAccessible(true);
            Object val = field.get(obj);
            if (val == null) return null;
            return (T) val;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @return
     */
    public static boolean hasAnnotation(Object o, String fieldName) {
        if (o == null) return false;
        Class<?> clazz = o.getClass();
        Field field = getFileByColumnName(clazz, fieldName);
        if (field != null) return true;
        return false;
    }


    public static Object getFieldByAnnotation(Object o, String fieldName) {
        if (o == null) return false;
        Class<?> clazz = o.getClass();
        Field field = getFileByColumnName(clazz, fieldName);
        if (field == null) new Throwable("field no found");
        return getValByNumberVar(o, field.getName());
    }

    public static <T> T getFieldValueT(Object obj, String fieldName) {
        Object result = null;
        Field field = ReflectUtil.getField(obj, fieldName);
        if (field != null) {
            field.setAccessible(true);
            try {
                result = field.get(obj);
                if (result == null) return null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return (T) result;
        }
        return null;
    }

    /**
     * 利用反射获取指定对象的指定属性
     *
     * @param obj       目标对象
     * @param fieldName 目标属性
     * @return 目标属性的值
     */
    public static Object getFieldValue(Object obj, String fieldName) {
        Object result = null;
        Field field = ReflectUtil.getField(obj, fieldName);
        if (field != null) {
            field.setAccessible(true);
            try {
                result = field.get(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
        return null;
    }


    /**
     * 利用反射获取指定对象里面的指定属性
     *
     * @param obj       目标对象
     * @param fieldName 目标属性
     * @return 目标字段
     */

    private static Field getField(Object obj, String fieldName) {
        Field field = null;
        for (Class<?> clazz = obj.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException e) {
                //这里不用做处理，子类没有该字段可能对应的父类有，都没有就返回null。
            }
        }
        return field;
    }


    /**
     * 利用反射设置指定对象的指定属性为指定的值
     *
     * @param obj        目标对象
     * @param fieldName  目标属性
     * @param fieldValue 目标值
     */
    public static void setFieldValue(Object obj, String fieldName, Object fieldValue) {
        Field field = ReflectUtil.getField(obj, fieldName);
        if (field != null) {
            try {
                field.setAccessible(true);
                field.set(obj, fieldValue);
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static void setFieldValueByAlias(Object obj, String aliasName, Object fieldValue) {
        Field field = ReflectUtil.getFileByAliasName(obj.getClass(), aliasName);
        if (field != null) {
            try {
                field.setAccessible(true);
                field.set(obj, fieldValue);
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static Object setFieldValues(Object obj, Map<String, Object> attr) {
        Object val;
        for (String field : attr.keySet()) {
            val = attr.get(field);
            setFieldValue(obj, field, val);
        }
        return obj;
    }

    public static Object setFieldsValueSmart(Object obj, Map<String, Object> attr) {
        Object fieldValue;
        Field field;
        for (String name : attr.keySet()) {
            field = getFieldByName(obj.getClass(), name);
            if (field == null) field = getFileByColumnName(obj.getClass(), name);
            if (field == null) field = getFileByAliasName(obj.getClass(), name);
            fieldValue = attr.get(name);
            if (field != null) field.setAccessible(true);
            try {
                field.set(obj, fieldValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return obj;
    }

    public static Object setFieldValueSmart(Object obj, String name, Object fieldValue) {
        Field field;
        field = getFieldByName(obj.getClass(), name);
        if (field == null) field = getFileByColumnName(obj.getClass(), name);
        if (field == null) field = getFileByAliasName(obj.getClass(), name);
        if (field != null) field.setAccessible(true);
        try {
            field.set(obj, fieldValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static List<Field> getIterationFieldsByOrder(Class clazz) {
        String[] columnArray = columnArray(clazz);
        List<Field> fields = getIterationColumn(clazz);
        List<Field> _fields = new ArrayList<>();
        for (int i = 0; i < columnArray.length; i++) {
            String fieldName = columnArray[i];
            for (int j = 0; j < fields.size(); j++) {
                Field field = fields.get(j);
                if (fieldName.equals(field.getName())) {
                    _fields.add(field);
                    break;
                }
            }
        }
        return _fields;
    }

    public static List<Field> getIterationColumn(Class clazz) {
        List<Field> fields = new ArrayList<>();
        Class tempClass = clazz;
        while (tempClass != null && !tempClass.getName().toLowerCase().equals("java.lang.object")) {
            fields.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            tempClass = tempClass.getSuperclass();
        }
        Field field;
        List<Field> _fields = new ArrayList<>();
        for (int i = 0; i < fields.size(); i++) {
            field = fields.get(i);
            if (field.isAnnotationPresent(Column.class)) {
                _fields.add(field);
            }
        }
        return _fields;
    }


    public static List<Field> getIterationColumnByOrder(Class clazz) {
        String[] columnArray = columnArray(clazz);
        List<Field> fields = getIterationFields(clazz);
        List<Field> _fields = new ArrayList<>();
        for (int i = 0; i < columnArray.length; i++) {
            String fieldName = columnArray[i];
            for (int j = 0; j < fields.size(); j++) {
                Field field = fields.get(j);
                if (fieldName.equals(field.getName())) {
                    _fields.add(field);
                    break;
                }
            }
        }
        return _fields;
    }

    public static boolean hasClass(String classPath) {
        try {
            if (Class.forName(classPath) != null) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public static <T> T newInstance(String classPath, Object[] params) {
        try {
            Class clazz = Class.forName(classPath);
            return newInstance(clazz, params);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T newInstance(Class clazz, Object... params) {
        //获取参数为String的构造函数
        Constructor[] constructors = clazz.getConstructors();
        Constructor constructor;
        T t = null;
        boolean compare = true;
        try {
            for (int i = 0; i < constructors.length; i++) {
                constructor = constructors[i];
                Class[] types = constructor.getParameterTypes();
                if ((params == null || params.length == 0) &&
                        (types == null || types.length == 0)) {
                    t = (T) clazz.newInstance();
                    break;
                }
                compare = constructorCompare(types, params);
                if (compare) {
                    t = (T) constructor.newInstance(params);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 基于反射的构造函数匹配
     * 构造函数匹配
     *
     * @return
     */
    public static boolean constructorCompare(Class[] types, Object... params) {
        if (params != null && types != null && (params.length == types.length)) {
            boolean compare;
            for (int i = 0; i < types.length; i++) {
                compare = forceConvert(types[i], params[i]);
                if (!compare) return false;
            }
            return true;
        } else if (params == null && types == null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param type
     * @param val
     * @return
     */
    public static boolean forceConvert(Class type, Object val) {
        try {
            Class objType = val.getClass();
            if ((type == Integer.class || type == int.class) && (objType == Integer.class || objType == int.class))
                return true;
            if ((type == Double.class || type == double.class) && (objType == Double.class || objType == double.class))
                return true;
            if ((type == Float.class || type == float.class) && (objType == Float.class || objType == float.class))
                return true;
            if ((type == Short.class || type == short.class) && (objType == Short.class || objType == short.class))
                return true;
            if ((type == Long.class || type == long.class) && (objType == Long.class || objType == long.class))
                return true;
            if ((type == Boolean.class || type == boolean.class) && (objType == Boolean.class || objType == boolean.class))
                return true;
            if ((type == Byte.class || type == byte.class) && (objType == Byte.class || objType == byte.class))
                return true;
            if ((type == Character.class || type == char.class) && (objType == Character.class || objType == char.class))
                return true;
            objType.cast(val);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
