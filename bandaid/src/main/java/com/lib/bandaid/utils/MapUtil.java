package com.lib.bandaid.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by zy on 2016/11/23.
 * Map<K-V>工具类
 */

public class MapUtil {

    private MapUtil() {
    }

    /**
     * ListMap 转 Map，前提是key不会重复，否则后果自负
     *
     * @return
     */
    public static Map listMap2Map(List<Map> list) {
        if (list == null) return null;
        HashMap map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            Map temp = list.get(i);
            map.putAll(temp);
        }
        return map;
    }

    public static <T> T getListMapValEasyByKey(List<Map> list, String key) {
        Map map = listMap2Map(list);
        if (map == null) return null;
        return (T) map.get(key);
    }

    /**
     * 合并两个Map
     *
     * @return
     */
    public static Map<String, Object> merge2Map(Map<String, Object> map1, Map<String, Object> map2) {
        Map<String, Object> map = new HashMap<>();
        map.putAll(map1);
        map.putAll(map2);
        return map;
    }

    /**
     * Map的拆分
     *
     * @param map
     * @param s
     * @return
     */
    public List<Map<String, Object>> splitMap(Map<String, Object> map, String[] s) {
        List<Map<String, Object>> list = null;
        int flag = 0;
        for (int i = 0; i <= s.length; i++) {
            Map<String, Object> temp = new HashMap<>();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                temp.put(entry.getKey(), entry.getValue());
                flag++;
                if (entry.getKey().equals(s[i])) {
                    break;
                }

                System.out.println(entry.getKey() + "--->" + entry.getValue());
            }
            list.add(temp);
        }
        return null;
    }

    private static Gson gson;

    public static Map<String, String> entity2Map(String key, Object obj) {
        if (gson == null) {
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        }
        String value = gson.toJson(obj);
        Map<String, String> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    public static String entity2Json(Object obj) {
        if (gson == null) {
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        }
        String value = gson.toJson(obj);
        return value;
    }

    public static <T> T string2Entity(String json, Class clazz) {
        if (gson == null) {
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        }
        T t = (T) gson.fromJson(json, clazz);
        return t;
    }

    public static List<Map> obj2ListMap(Object obj) {
        String json = entity2Json(obj);
        return string2ListMap(json);
    }

    public static List<Map> string2ListMap(String json) {
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(
                            new TypeToken<Map<String, Object>>() {
                            }.getType(),
                            new JsonDeserializer<Map<String, Object>>() {
                                @Override
                                public Map<String, Object> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                                    Map<String, Object> map = new HashMap<>();
                                    JsonObject jsonObject = json.getAsJsonObject();
                                    Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
                                    JsonElement jsonElement;
                                    for (Map.Entry<String, JsonElement> entry : entrySet) {
                                        jsonElement = entry.getValue();
                                        if (jsonElement == null) continue;
                                        if (jsonElement.isJsonNull()) {
                                            map.put(entry.getKey(), jsonElement.getAsJsonNull());
                                        }
                                        if (jsonElement.isJsonPrimitive()) {
                                            JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
                                            if (jsonPrimitive.isNumber()) {
                                                Number number = jsonPrimitive.getAsNumber();
                                                if (number == null) continue;
                                                map.put(entry.getKey(), jsonPrimitive.getAsInt());
                                            }
                                            if (jsonPrimitive.isString()) {
                                                map.put(entry.getKey(), jsonPrimitive.getAsString());
                                            }
                                            if (jsonPrimitive.isBoolean()) {
                                                map.put(entry.getKey(), jsonPrimitive.getAsBoolean());
                                            }
                                            if (jsonPrimitive.isJsonNull()) {
                                                map.put(entry.getKey(), jsonPrimitive.getAsJsonNull());
                                            }
                                        }
                                        if (jsonElement.isJsonObject()) {
                                            map.put(entry.getKey(), jsonElement.getAsJsonNull());
                                        }
                                        if (jsonElement.isJsonArray()) {
                                            JsonArray jsonArray = jsonElement.getAsJsonArray();
                                            if (jsonArray == null) continue;
                                            JsonElement _jsonElement;
                                            Iterator<JsonElement> iterator = jsonArray.iterator();
                                            while (iterator.hasNext()) {
                                                _jsonElement = iterator.next();
                                                if (_jsonElement == null) continue;
                                                if (_jsonElement.isJsonPrimitive()) {
                                                    JsonPrimitive _jsonPrimitive = _jsonElement.getAsJsonPrimitive();
                                                    if (_jsonPrimitive.isNumber()) {
                                                        Number number = _jsonPrimitive.getAsNumber();
                                                        if (number == null) continue;
                                                        map.put(entry.getKey(), _jsonPrimitive.getAsInt());
                                                    }
                                                    if (_jsonPrimitive.isString()) {
                                                        map.put(entry.getKey(), _jsonPrimitive.getAsString());
                                                    }
                                                    if (_jsonPrimitive.isBoolean()) {
                                                        map.put(entry.getKey(), _jsonPrimitive.getAsBoolean());
                                                    }
                                                    if (_jsonPrimitive.isJsonNull()) {
                                                        map.put(entry.getKey(), _jsonPrimitive.getAsJsonNull());
                                                    }
                                                }
                                            }
                                            map.put(entry.getKey(), jsonElement.getAsJsonNull());
                                        }
                                    }
                                    return map;
                                }
                            }).create();
            List<Map> res = gson.fromJson(json, new TypeToken<List<Map>>() {
            }.getType());
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Map<String, Object>> string2ListMap1(String json) {
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(
                            new TypeToken<TreeMap<String, Object>>() {
                            }.getType(),
                            new JsonDeserializer<TreeMap<String, Object>>() {
                                @Override
                                public TreeMap<String, Object> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                                    TreeMap<String, Object> treeMap = new TreeMap<>();
                                    JsonObject jsonObject = json.getAsJsonObject();
                                    Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
                                    for (Map.Entry<String, JsonElement> entry : entrySet) {
                                        treeMap.put(entry.getKey(), entry.getValue());
                                    }
                                    return treeMap;
                                }
                            }).create();
            List<Map<String, Object>> res =
                    gson.fromJson(json, new TypeToken<List<TreeMap<String, Object>>>() {
                    }.getType());
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static <T> T convert(Object obj, Type type) {
        if (obj instanceof List) {
            Gson gson = GsonFactory.getFactory().getComGson();
            String json = gson.toJson(obj);
            return (T) gson.fromJson(json, type);
        } else {
            if (obj == null) return null;
            Gson gson = GsonFactory.getFactory().getComGson();
            String json = gson.toJson(obj);
            return (T) gson.fromJson(json, type);
        }
    }


    public static Map<String, Object> str2MapRes(String json) {
        Map<String, Object> res = null;
        JSONObject jsonObject;
        try {
            res = new HashMap<>();
            jsonObject = new JSONObject(json);
            Integer code = jsonObject.getInt("code");
            res.put("code", code);
            String msg = jsonObject.getString("msg");
            res.put("msg", msg);
            if (jsonObject.isNull("data")) {
                res.put("data", null);
            } else {
                Object data = jsonObject.get("data");
                res.put("data", data.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static Map<String, Object> str2MapRes(Object json) {
        Map<String, Object> res = null;
        JSONObject jsonObject;
        try {
            res = new HashMap<>();
            jsonObject = new JSONObject(json.toString());
            Integer code = jsonObject.getInt("code");
            res.put("code", code);
            String msg = jsonObject.getString("msg");
            res.put("msg", msg);
            if (jsonObject.isNull("data")) {
                res.put("data", null);
            } else {
                Object data = jsonObject.get("data");
                res.put("data", data.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }


    /**
     * Map转成实体对象
     *
     * @param map   map实体对象包含属性
     * @param clazz 实体对象类型
     * @return
     */
    public static Object map2Object(Map<String, Object> map, Class<?> clazz) {
        if (map == null) {
            return null;
        }
        Object obj = null;
        try {
            obj = clazz.newInstance();

            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                field.setAccessible(true);
                field.set(obj, map.get(field.getName()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * Map转成实体对象
     *
     * @param map   map实体对象包含属性
     * @param clazz 实体对象类型
     * @return
     */
    public static <T> T map2Entity(Map<String, Object> map, Class<?> clazz) {
        if (map == null) {
            return null;
        }
        Object obj = null;
        try {
            obj = clazz.newInstance();
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                field.setAccessible(true);
                field.set(obj, map.get(field.getName()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) obj;
    }

    public static <T>Map<String,Object> classField2MapStr(List<T> list, String key, String value) {
        if (list == null || list.size() == 0) return null;
        T t;
        Class<?> type = null;
        String item1;
        Object item2;
        Map<String,Object> res = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            t = list.get(i);
            item1 = ReflectUtil.getFieldValueT(t, key);
            item2 = ReflectUtil.getFieldValueT(t, value);
            res.put(item1,item2);
        }
        return res;

    }
}
