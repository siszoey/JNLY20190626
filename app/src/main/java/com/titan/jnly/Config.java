package com.titan.jnly;

import android.os.Environment;

import java.io.File;

public final class Config extends Config_dev {

    public static String APP_SPATIAL_DIR = "spatial";
    public static String APP_CACHE_DIR = "cache";
    public static String APP_CRASH_DIR= "crash";

    public static String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
    public static String APP_PATH = ROOT_PATH.concat(File.separator).concat(APP_PATH_NAME);
    public static String APP_DB_PATH = APP_PATH.concat(File.separator).concat(APP_DB_NAME);
    public static String APP_SDB_PATH = APP_PATH.concat(File.separator).concat(APP_SPATIAL_DIR);
    public static String APP_PATH_CRASH = APP_PATH.concat(File.separator).concat(APP_CRASH_DIR);
    public static String APP_MAP_CACHE = APP_PATH.concat(File.separator).concat(APP_CACHE_DIR);


    private Config() {
    }
}
