package com.lib.bandaid.data.remote.entity;

import com.lib.bandaid.widget.collect.image.CollectImgBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TTFileResult implements Serializable {

    private String pathName;
    private String title;
    private String size;
    private String createDate;

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }


    /**
     * *********************************************************************************************
     * *********************************************************************************************
     * *********************************************************************************************
     */

    public static List<String> convertUrl(String root, List<TTFileResult> results) {
        if (results == null || results.size() == 0) return null;
        if (root.endsWith("/")) root = root.substring(0, root.length() - 1);
        List<String> urls = new ArrayList<>();
        String uri;
        String path;
        for (TTFileResult result : results) {
            path = result.getPathName();
            if (path.startsWith("/")) path = path.substring(1);
            uri = root + "/" + path;
            urls.add(uri);
        }
        return urls;
    }

    public static List<CollectImgBean> convertImgBean(String root, List<TTFileResult> results) {
        if (results == null || results.size() == 0) return null;
        if (root.endsWith("/")) root = root.substring(0, root.length() - 1);
        String uri;
        String path;
        CollectImgBean bean;
        List<CollectImgBean> beans = new ArrayList<>();
        for (TTFileResult result : results) {
            path = result.getPathName();
            if (path.startsWith("/")) path = path.substring(1);
            uri = root + "/" + path;
            bean = new CollectImgBean();
            bean.setUri(uri);
            bean.setTag(result);
            beans.add(bean);
        }
        return beans;
    }
}
