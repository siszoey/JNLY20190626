package com.lib.bandaid.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonAdapter extends TypeAdapter<Object> {


    @Override
    public Object read(JsonReader in) throws IOException {
        // 反序列化
        JsonToken token = in.peek();
        switch (token) {
            case BEGIN_ARRAY:
                List<Object> list = new ArrayList<Object>();
                in.beginArray();
                while (in.hasNext()) {
                    list.add(read(in));
                }
                in.endArray();
                return list;
            case BEGIN_OBJECT:
                Map<String, Object> map = new HashMap<String, Object>();
                in.beginObject();
                while (in.hasNext()) {
                    map.put(in.nextName(), read(in));
                }
                in.endObject();
                return map;
            case STRING:
                return in.nextString();
            case NUMBER:
                String num = in.nextString();
                double dbNum = Double.parseDouble(num);
                if (num.contains(".")) {
                    return dbNum;
                } else {
                    if (dbNum > Long.MAX_VALUE) {
                        return dbNum;
                    } else {
                        return (long) dbNum;
                    }
                }
            case BOOLEAN:
                return in.nextBoolean();
            case NULL:
                in.nextNull();
                return null;
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public void write(JsonWriter out, Object value) throws IOException {
    }
}

