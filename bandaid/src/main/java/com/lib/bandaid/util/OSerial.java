package com.lib.bandaid.util;

import android.content.Intent;

import java.io.Serializable;

public class OSerial<T> implements Serializable {

    public final static String key = "OSerial_KEY";

    public static <T> OSerial<T> create(Object data) {
        OSerial<T> serial = new OSerial(data);
        return serial;
    }

    private String data;

    public OSerial(T data) {
        setData(data);
    }

    public T getData(Class clazz) {
        return ObjectUtil.convert(data, clazz);
    }

    public void setData(T data) {
        this.data = JsonUtil.obj2Json(data);
    }

    //----------------------------------------------------------------------------------------------
    public static <T> T getData(Intent intent, Class clazz) {
        OSerial<T> oSerial = (OSerial<T>) intent.getSerializableExtra(OSerial.key);
        return oSerial.getData(clazz);
    }

    public static <T> void putSerial(Intent intent, T t) {
        intent.putExtra(OSerial.key, OSerial.create(t));
    }
}
