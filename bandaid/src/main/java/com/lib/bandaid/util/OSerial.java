package com.lib.bandaid.util;

import java.io.Serializable;

public class OSerial<T> implements Serializable {

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
}
