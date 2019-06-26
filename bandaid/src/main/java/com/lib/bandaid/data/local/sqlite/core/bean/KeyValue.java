package com.lib.bandaid.data.local.sqlite.core.bean;

public class KeyValue {
    /**
     * key
     */
    private String key;
    /**
     * value
     */
    private Object value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
