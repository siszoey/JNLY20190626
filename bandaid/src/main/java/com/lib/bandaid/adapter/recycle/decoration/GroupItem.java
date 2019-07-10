package com.lib.bandaid.adapter.recycle.decoration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author：byd666 on 2017/12/2 15:31
 */

public class GroupItem<T> implements Serializable {

    String name;

    int icon;

    T data;

    Object tag;

    public GroupItem() {
    }

    public GroupItem(String name, T data) {
        this.name = name;
        this.data = data;
    }

    public GroupItem(String name, int icon, T data) {
        this.name = name;
        this.icon = icon;
        this.data = data;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    /**
     * *********************************************************************************************
     */
    public static <T> List<GroupItem<T>> convert(String name, List<T> ts) {
        List<GroupItem<T>> res = new ArrayList<>();
        GroupItem item;
        for (int i = 0; i < ts.size(); i++) {
            item = new GroupItem(name, ts.get(i));
            res.add(item);
        }
        return res;
    }

    public static <T> List<GroupItem<T>> convert(String groupName, Object tag, List<T> ts) {
        List<GroupItem<T>> res = new ArrayList<>();
        GroupItem item;
        for (int i = 0; i < ts.size(); i++) {
            item = new GroupItem(groupName, ts.get(i));
            item.setTag(tag);
            res.add(item);
        }
        return res;
    }
}
