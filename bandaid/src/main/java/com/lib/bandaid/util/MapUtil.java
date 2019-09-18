package com.lib.bandaid.util;

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

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
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

public final class MapUtil {

    private MapUtil() {
    }

    public static Map itemClone(Map src, String... keys) {
        Map<String, Object> trg = new HashMap<>();
        for (Object key : src.keySet()) {
            for (String _key : keys) {
                if (key.toString().equals(_key)) {
                    trg.put(_key, src.get(key));
                }
            }
        }
        return trg;
    }

    public static Map itemClone(Map src, ItemListen itemListen, String... keys) {
        Map<String, Object> trg = new HashMap<>();
        Object val;
        for (Object key : src.keySet()) {
            for (String _key : keys) {
                if (key.toString().equals(_key)) {
                    val = src.get(key);
                    if (itemListen != null) val = itemListen.itemListen(_key, val);
                    trg.put(_key, val);
                }
            }
        }
        return trg;
    }

    public interface ItemListen {
        public Object itemListen(String key, Object val);
    }
}
