package com.lib.bandaid.widget.collect.image;

import com.camera.lib.widget.ImagePagerBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lib.bandaid.rw.file.utils.FileUtil;
import com.lib.bandaid.utils.MapUtil;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CollectImgBean implements Serializable {

    private String name;

    private String uri;

    private String desc;

    private String date;

    public CollectImgBean() {
    }

    public CollectImgBean(String name, String uri, String desc, String date) {
        this.name = name;
        this.uri = uri;
        this.desc = desc;
        this.date = date;
    }

    public String getName() {
        if (name == null) return FileUtil.getFileNameByPath(uri);
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getDesc() {
        if (desc == null) return FileUtil.getFileNameByPath(uri);
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "CollectImgBean{" +
                "name='" + name + '\'' +
                ", uri='" + uri + '\'' +
                ", desc='" + desc + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------

    public static ArrayList<CollectImgBean> convertFromJson(String json) {
        Type type = new TypeToken<ArrayList<CollectImgBean>>() {
        }.getType();
        ArrayList<CollectImgBean> beans = new Gson().fromJson(json, type);
        return beans;
    }

    public static List<File> obtainFiles(String json) {
        ArrayList<CollectImgBean> beans = convertFromJson(json);
        if (beans == null || beans.size() == 0) return null;
        List<File> files = new ArrayList<>();
        for (CollectImgBean bean : beans) {
            files.add(new File(bean.getUri()));
        }
        return files;
    }

    public static List<CollectImgBean> convertFromList(List<String> paths) {
        if (paths == null) return null;
        CollectImgBean bean;
        List<CollectImgBean> beans = new ArrayList<>();
        for (String path : paths) {
            bean = new CollectImgBean();
            bean.setUri(path);
            beans.add(bean);
        }
        return beans;
    }

    public static ArrayList<ImagePagerBean> convertToScanList(List<CollectImgBean> beans) {
        if (beans == null) return null;
        ImagePagerBean scan;
        ArrayList<ImagePagerBean> scans = new ArrayList<>();
        for (CollectImgBean bean : beans) {
            scan = new ImagePagerBean();
            scan.setDesc(bean.getDesc());
            scan.setUrl(bean.getUri());
            scans.add(scan);
        }
        return scans;
    }

    public static List<CollectImgBean> convertToWorkDir(String workDir, List<CollectImgBean> beans) {
        if (beans == null || beans.size() == 0) return null;
        String oldPath;
        String newPath;
        String suffix;
        for (CollectImgBean bean : beans) {
            oldPath = bean.getUri();
            if (!oldPath.startsWith(workDir)) {//复制图片到采集目录
                suffix = FileUtil.getFileExtendNameByPath(oldPath);
                newPath = FileUtil.fileRandomPath(workDir, "copy", suffix);
                //boolean res = BitmapUtil.zipImgQuality(oldPath, newPath);
                boolean res = FileUtil.copyFile(oldPath, newPath);
                if (res) bean.setUri(newPath);
            }
        }
        return beans;
    }
}
