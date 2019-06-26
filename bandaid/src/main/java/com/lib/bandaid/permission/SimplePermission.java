package com.lib.bandaid.permission;

import android.Manifest;

/**
 * Created by zy on 2019/4/11.
 */

public class SimplePermission {

    public static final int PERMISSION_REQ = 0x99999;

    /**
     * *********************************************************************************************
     * 常用权限
     * *********************************************************************************************
     */
    //文件夹读写权限
    public final static String[] MANIFEST_PHONE_STATE = new String[]{Manifest.permission.READ_PHONE_STATE};
    //文件夹读写权限
    public final static String[] MANIFEST_STORAGE = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    //安装apk权限
    public final static String[] MANIFEST_INSTALL_APK = new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES};
    //定位权限
    public final static String[] MANIFEST_LOCATION = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.INTERNET
    };
    //相机权限
    public final static String[] MANIFEST_CAMERA = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

}
