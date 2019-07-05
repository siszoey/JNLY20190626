package com.camera.lib.widget;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Description：
 * Author：LiuYM
 * Date： 2017-04-22 15:16
 */

public class ImagePagerBean implements Serializable {
    private String mUrl;
    private String mDesc;
    private String mSmallUrl;

    public ImagePagerBean(File file) {
        mUrl = file.getPath();
        mDesc = file.getName();
        this.mSmallUrl = mUrl;
    }

    public ImagePagerBean(String url, String desc, String smallUrl) {
        mUrl = url;
        mDesc = desc;
        this.mSmallUrl = smallUrl;
    }


    public String getSmallUrl() {
        return mSmallUrl;
    }

    public void setSmallUrl(String smallUrl) {
        mSmallUrl = smallUrl;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getDesc() {
        return mDesc;
    }

    public void setDesc(String desc) {
        mDesc = desc;
    }


    public static ArrayList<ImagePagerBean> convert2Beans(List<File> files) {
        ArrayList<ImagePagerBean> mImages = new ArrayList<>();
        ImagePagerBean bean;
        for (int i = 0; i < files.size(); i++) {
            bean = new ImagePagerBean(files.get(i));
            mImages.add(bean);
        }
        return mImages;
    }
}
