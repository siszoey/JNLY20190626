package com.lib.bandaid.data.local.sqlite.utils;

import android.database.Cursor;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lib.bandaid.data.local.sqlite.core.annotation.Column;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 游标转实体
 * Created by zy on 2016/12/23.
 */

public final class CursorUtil<T> {

    private CursorUtil() {

    }

    /**
     * 游标转json
     *
     * @param cursor
     * @return
     */
    public static JSONArray cursor2Json(Cursor cursor) {
        JSONArray resultSet = new JSONArray();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();
            for (int i = 0; i < totalColumn; i++) {
                if (cursor.getColumnName(i) != null) {
                    try {
                        String key = cursor.getColumnName(i);
                        if (cursor.getString(i) != null) {
                            Log.d("TAG_NAME", cursor.getString(i));
                            rowObject.put(key, cursor.getString(i));
                        } else {
                            rowObject.put(key, "");
                        }
                    } catch (Exception e) {
                        Log.d("TAG_NAME", e.getMessage());
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }
        cursor.close();
        cursor = null;
        Log.d("TAG_NAME", resultSet.toString());
        return resultSet;
    }

    /**
     * 借助第三方转化
     *
     * @param cursor
     * @param clazz
     * @return
     */
    public static Object cursor2Json2Entity(Cursor cursor, Class clazz) {
        Object object = null;
        String jsonStr = null;
        try {
            jsonStr = cursor2Json(cursor).get(0).toString();
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            object = gson.fromJson(jsonStr, clazz);//转json
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            cursor = null;
        }
        return object;
    }

    public static List<?> cursor2Json2List(Cursor cursor, Class clazz) {
        List<Object> list = null;
        try {
            list = new ArrayList<Object>();
            JSONArray jsonArray = cursor2Json(cursor);
            System.out.println(jsonArray.toString());
            if (null != jsonArray && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    System.out.println(jsonArray.get(i).toString());
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    Object object = new Object();
                    object = gson.fromJson(jsonArray.get(i).toString(), clazz);//转json

                    System.out.println(object.toString());
                    list.add(object);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            cursor = null;
        }
        return list;
    }

    public static Map string2Map(String jsonStr) throws Exception {
        JSONObject jsonObj = new JSONObject(jsonStr);
        Iterator<String> nameItr = jsonObj.keys();
        String name;
        Map<String, Object> outMap = new HashMap<String, Object>();
        while (nameItr.hasNext()) {
            name = nameItr.next();
            outMap.put(name, jsonObj.getString(name));
        }
        return outMap;
    }

    /**
     * @param clazz
     * @param cursor
     * @return
     */
    public static Object cursor2Entity(Class<?> clazz, Cursor cursor) {
        Object o = null;
        try {
            JSONArray jsonArray = cursor2Json(cursor);
            if (jsonArray == null || jsonArray.length() == 0) {
                o = null;
            }
            //Field[] fields = clazz.getDeclaredFields();
            List<Field> fields = ReflectUtil.getIterationFields(clazz);
            Integer length = jsonArray.length();
            if (length == 1) { //转成单个实体
                for (int i = 0; i < length; i++) {
                    Map<String, String> map = string2Map(jsonArray.get(i).toString());
                    o = clazz.newInstance();
                    for (int j = 0; j < fields.size(); j++) {
                        Field field = fields.get(j);
                        String fieldName = field.getName();
                        Method setMethod = ReflectUtil.setMethod(clazz, field);
                        if (null != setMethod) {
                            Object value = TransferUtil.string2SimpleObject(field, map.get(fieldName));
                            setMethod.invoke(o, value);//
                        }
                    }
                }
            } else {//转成集合

            }

        } catch (Exception e) {
            e.printStackTrace();
            o = null;
        } finally {
            cursor.close();
            cursor = null;
        }
        return o;
    }

    /**
     * @param clazz
     * @param cursor
     * @return
     */
    public static Object cursor2List(Class<?> clazz, Cursor cursor) {
        Object o = null;
        List<Object> list = null;
        try {
            JSONArray jsonArray = cursor2Json(cursor);
            if (jsonArray == null || jsonArray.length() == 0) {
                o = null;
            }
            List<Field> fields = ReflectUtil.getIterationFields(clazz);
            Integer length = jsonArray.length();
            list = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                Map<String, String> map = string2Map(jsonArray.get(i).toString());
                o = clazz.newInstance();
                for (int j = 0; j < fields.size(); j++) {
                    Field field = fields.get(i);
                    String fieldName = field.getName();
                    Method setMethod = ReflectUtil.setMethod(clazz, field);
                    if (null != setMethod) {
                        Object value = TransferUtil.string2SimpleObject(field, map.get(fieldName));
                        setMethod.invoke(o, new Object[]{value});//获取属性值
                    }
                }
                list.add(o);
            }
        } catch (Exception e) {
            e.printStackTrace();
            list = null;
        } finally {
            cursor.close();
            cursor = null;
        }
        return list;
    }


    /**
     * @param clazz
     * @param cursor
     * @return
     */
    public static Object cursor2EntityV1(Class<?> clazz, Cursor cursor) {
        Object o = null;
        try {
            List<Field> fields = ReflectUtil.getIterationFields(clazz);
            Field field;
            String fieldName;
            Method setMethod;
            int index;
            String v;
            Object value;
            while (cursor.moveToNext()) {
                o = clazz.newInstance();
                for (int i = 0; i < fields.size(); i++) {
                    field = fields.get(i);
                    fieldName = field.getName();
                    if (field.isAnnotationPresent(Column.class)) {
                        setMethod = ReflectUtil.setMethod(clazz, field);
                        if (null != setMethod) {
                            index = cursor.getColumnIndex(fieldName);
                            v = cursor.getString(index);
                            value = TransferUtil.string2SimpleObject(field, v);
                            setMethod.invoke(o, value);
                        }
                    }
                }
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            o = null;
        } finally {
            cursor.close();
            cursor = null;
        }
        return o;
    }

    /**
     * @param clazz
     * @param cursor
     * @return
     */
    public static Object cursor2EntityV2(Class<?> clazz, Cursor cursor, boolean idDbColumn) {
        Object o = null;
        try {
            List<Field> fields = ReflectUtil.getIterationFields(clazz);
            Field field;
            String fieldName;
            Method setMethod;
            int index;
            String v;
            Object value;
            while (cursor.moveToNext()) {
                o = clazz.newInstance();
                for (int i = 0; i < fields.size(); i++) {
                    field = fields.get(i);
                    if (idDbColumn) {
                        fieldName = ReflectUtil.getColumnName(field);
                    } else {
                        fieldName = field.getName();
                    }
                    if (field.isAnnotationPresent(Column.class)) {
                        setMethod = ReflectUtil.setMethod(clazz, field);
                        if (null != setMethod) {
                            index = cursor.getColumnIndex(fieldName);
                            v = cursor.getString(index);
                            value = TransferUtil.string2SimpleObject(field, v);
                            setMethod.invoke(o, value);
                        }
                    }
                }
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            o = null;
        } finally {
            cursor.close();
            cursor = null;
        }
        return o;
    }

    /**
     * @param clazz
     * @param cursor
     * @return
     */
    public static Object cursor2ListV1(Class<?> clazz, Cursor cursor) {
        Object o = null;
        List<Object> list = null;
        try {
            list = new ArrayList<>();
            List<Field> fields = ReflectUtil.getIterationFields(clazz);
            Field field;
            String fieldName;
            Method setMethod;
            int index;
            String v;
            Object value;
            while (cursor.moveToNext()) {
                o = clazz.newInstance();
                for (int i = 0; i < fields.size(); i++) {
                    field = fields.get(i);
                    fieldName = field.getName();
                    if (field.isAnnotationPresent(Column.class)) {
                        setMethod = ReflectUtil.setMethod(clazz, field);
                        if (null != setMethod) {
                            index = cursor.getColumnIndex(fieldName);
                            v = cursor.getString(index);
                            value = TransferUtil.string2SimpleObject(field, v);
                            setMethod.invoke(o, value);
                        }
                    }
                }
                list.add(o);
            }
        } catch (Exception e) {
            e.printStackTrace();
            list = null;
        } finally {
            cursor.close();
            cursor = null;
        }
        return list;
    }

    /**
     * @param clazz
     * @param cursor
     * @return
     */
    public static Object cursor2ListV3(Class<?> clazz, Cursor cursor, boolean idDbColumn) {
        Object o = null;
        List<Object> list = null;
        try {
            list = new ArrayList<>();
            List<Field> fields = ReflectUtil.getIterationFields(clazz);
            Field field;
            String fieldName;
            Method setMethod;
            int index;
            String v;
            Object value;
            while (cursor.moveToNext()) {
                o = clazz.newInstance();
                for (int i = 0; i < fields.size(); i++) {
                    field = fields.get(i);
                    if (field.isAnnotationPresent(Column.class)) {
                        if (idDbColumn) {
                            fieldName = ReflectUtil.getColumnName(field);
                        } else {
                            fieldName = field.getName();
                        }

                        setMethod = ReflectUtil.setMethod(clazz, field);
                        if (null != setMethod) {
                            index = cursor.getColumnIndex(fieldName);
                            if (index == -1) continue;
                            v = cursor.getString(index);
                            value = TransferUtil.string2SimpleObject(field, v);
                            setMethod.invoke(o, value);
                        }
                    }
                }
                list.add(o);
            }
        } catch (Exception e) {
            e.printStackTrace();
            list = null;
        } finally {
            cursor.close();
            cursor = null;
        }
        return list;
    }

    /**
     * 游标转ListMap
     *
     * @param cursor
     * @return
     */
    public static Map<String, Object> cursor2Map(Cursor cursor) {
        Map<String, Object> map = new HashMap();
        try {
            while (cursor.moveToNext()) {
                int count = 0;
                int columnLength = cursor.getColumnCount();
                int columnType;
                String columnName = "";
                Object columnVal = null;
                for (int i = 0; i < columnLength; i++) {
                    columnName = cursor.getColumnName(i);
                    columnType = cursor.getType(i);
                    switch (columnType) {
                        case Cursor.FIELD_TYPE_STRING:
                            columnVal = cursor.getString(i);
                            break;
                        case Cursor.FIELD_TYPE_BLOB:
                            columnVal = cursor.getBlob(i);
                            break;
                        case Cursor.FIELD_TYPE_FLOAT:
                            columnVal = cursor.getFloat(i);
                            break;
                        case Cursor.FIELD_TYPE_INTEGER:
                            columnVal = cursor.getInt(i);
                            break;
                        case Cursor.FIELD_TYPE_NULL:
                            columnVal = cursor.getString(i);
                            count++;
                            break;
                        default:
                            columnVal = cursor.getString(i);
                            break;
                    }
                    map.put(columnName, columnVal);
                }
                //都为null则认为 这条记录不存在
                if (count == columnLength) {
                    map.clear();
                } else {

                }
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return map;
    }

    /**
     * 游标转ListMap
     *
     * @param cursor
     * @return
     */
    public static List<Map<String, Object>> cursor2ListMap(Cursor cursor) {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = null;
        try {
            while (cursor.moveToNext()) {
                int count = 0;
                map = new HashMap();
                int columnLength = cursor.getColumnCount();
                int columnType;
                String columnName = "";
                Object columnVal = null;
                for (int i = 0; i < columnLength; i++) {
                    columnName = cursor.getColumnName(i);
                    columnType = cursor.getType(i);
                    switch (columnType) {
                        case Cursor.FIELD_TYPE_STRING:
                            columnVal = cursor.getString(i);
                            break;
                        case Cursor.FIELD_TYPE_BLOB:
                            columnVal = cursor.getBlob(i);
                            break;
                        case Cursor.FIELD_TYPE_FLOAT:
                            columnVal = cursor.getFloat(i);
                            break;
                        case Cursor.FIELD_TYPE_INTEGER:
                            columnVal = cursor.getInt(i);
                            break;
                        case Cursor.FIELD_TYPE_NULL:
                            columnVal = cursor.getString(i);
                            count++;
                            break;
                        default:
                            columnVal = cursor.getString(i);
                            break;
                    }
                    map.put(columnName, columnVal);
                }
                if (count != columnLength) {
                    list.add(map);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return list;
    }
}
