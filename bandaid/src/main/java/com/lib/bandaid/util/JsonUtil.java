package com.lib.bandaid.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public final class JsonUtil {

    private static Gson gson;

    public static Gson getGSon() {
        if (gson != null) return gson;
        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .registerTypeAdapter(
                        new TypeToken<Map<String, Object>>() {
                        }.getType(), new JsonAdapter())
                .create();
        return gson;
    }

    public static boolean isBadJson(String json) {
        return !isJson(json);
    }

    public static boolean isJson(String json) {
        if (ObjectUtil.isEmpty(json)) return false;
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean isJsonObj(String json) {
        final char[] strChar = json.substring(0, 1).toCharArray();
        final char firstChar = strChar[0];
        if (firstChar == '{') {
            return true;
        } else if (firstChar == '[') {
            return false;
        } else {
            return null;
        }
    }

    public static Boolean isJsonArray(String json) {
        Boolean isJsonObj = isJsonObj(json);
        if (isJsonObj == null) return null;
        return !isJsonObj;
    }

    public static String obj2Json(Object obj) {
        String value = getGSon().toJson(obj);
        return value;
    }

    public static String obj2String(Object obj) {
        if (obj instanceof String) return (String) obj;
        String value = getGSon().toJson(obj);
        return value;
    }

    public static String toFormat(String json) {
        try {
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.toJson(jsonObject);
        } catch (Exception e) {
            return json;
        }
    }

    public static <T> T convertO(Object obj, Class clazz) {
        if (obj instanceof String && clazz == String.class) return (T) obj;
        String json = obj2String(obj);
        if (clazz == String.class) return (T) json;
        boolean isBadJson = isBadJson(json);
        if (isBadJson) return null;
        Boolean isJsonObj = isJsonObj(json);
        if (isJsonObj == null) return convertT(json, clazz);
        if (isJsonObj) {
            return convertT(json, clazz);
        } else {
            return (T) convertL(json, clazz);
        }
    }

    public static <T> T convertT(Object obj, Type type) {
        String json = obj2String(obj);
        try {
            T t = getGSon().fromJson(json, type);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> List<T> convertL(Object obj, Class clazz) {
        String json = obj2String(obj);
        try {
            List<T> list = getGSon().fromJson(json, new JsonListType(clazz));
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
