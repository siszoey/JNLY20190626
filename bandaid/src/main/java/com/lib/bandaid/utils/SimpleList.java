package com.lib.bandaid.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zy on 2018/6/14.
 */

public class SimpleList<T> extends ArrayList<T> {

    public SimpleList push(T t) {
        add(t);
        return this;
    }

    public SimpleList pushAll(List<T> list) {
        addAll(list);
        return this;
    }

    public List<T> toList() {
        return this;
    }
}
