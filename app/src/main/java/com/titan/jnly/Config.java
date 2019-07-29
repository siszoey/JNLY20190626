package com.titan.jnly;

import android.os.Environment;

import java.io.File;

public final class Config extends Config_pro {

    public static String APP_SPATIAL_DIR = "spatial";
    public static String APP_CACHE_DIR = "cache";
    public static String APP_DIC_DIR = "dic";
    public static String APP_PHOTO_DIR = "photo";
    public static String APP_CRASH_DIR = "crash";

    public static String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
    //app所在目录
    public static String APP_PATH = ROOT_PATH.concat(File.separator).concat(APP_PATH_DIR);
    //app空间数据库目录
    public static String APP_SDB_PATH = APP_PATH.concat(File.separator).concat(APP_SPATIAL_DIR);
    //app本地崩溃日志目录
    public static String APP_PATH_CRASH = APP_PATH.concat(File.separator).concat(APP_CRASH_DIR);
    //app系统字典文件夹
    public static String APP_PATH_DIC = APP_PATH.concat(File.separator).concat(APP_DIC_DIR);
    //app缓存目录
    public static String APP_MAP_CACHE = APP_PATH.concat(File.separator).concat(APP_CACHE_DIR);
    //app图片目录
    public static String APP_PHOTO_PATH = APP_PATH.concat(File.separator).concat(APP_PHOTO_DIR);
    //app数据库所在路径
    public static String APP_DB_PATH = APP_PATH.concat(File.separator).concat(APP_DB_NAME);

    /**
     * 业务布局模板
     */
    public static String[] GEO_TB_MODULE = new String[]{"geodb/tb_moudle/property_point.xml", "geodb/tb_moudle/property_area.xml"};
    /**
     * 系统字典表
     */
    public static String DIC_DB_MODULE = "sqlite/jnly.db";

    private Config() {
    }
}
