package com.lib.bandaid.bean.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lib.bandaid.rw.file.utils.FileUtil;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jsb on 2018/5/25 0025.
 */

public class CollectFile implements Serializable {

    private String name;

    private String path;

    private String desc;

    public CollectFile() {

    }

    public CollectFile(String path) {
        this.name = FileUtil.getFileFullNameByPath(path);
        this.path = path;
        this.desc = name;
    }

    public CollectFile(String name, String path, String desc) {
        this.name = name;
        this.path = path;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static List<CollectFile> formJson(String json) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        gson.serializeNulls();
        return gson.fromJson(json, new TypeToken<List<CollectFile>>() {
        }.getType());
    }

    public static String toJson(List<CollectFile> list) {
        if (list == null) return null;
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        gson.serializeNulls();
        return gson.toJson(list);
    }

    public String toJson() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        gson.serializeNulls();
        return gson.toJson(this);
    }
}
