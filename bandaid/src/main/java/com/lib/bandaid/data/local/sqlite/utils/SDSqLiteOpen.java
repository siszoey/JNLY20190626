package com.lib.bandaid.data.local.sqlite.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.lib.bandaid.data.local.sqlite.core.SDSQLiteOpenHelper;
import com.lib.bandaid.data.local.sqlite.proxy.transaction.DbManager;

import java.io.File;

/**
 * Created by zy on 2016/11/18.
 * 数据库操作类
 */

public class SDSqLiteOpen extends SDSQLiteOpenHelper {
    private final static int VERSION = 1;
    private String DB_NAME;
    private String DB_PATH;


    //数据库的构造函数，传递三个参数的
    public SDSqLiteOpen(Context context, String path, String name, boolean defaultCreate) {
        this(context, path, name, null, VERSION, defaultCreate);
        this.DB_PATH = path.concat(File.separator).concat(name);
        this.DB_NAME = name;
    }

    //数据库的构造函数，传递三个参数的
    public SDSqLiteOpen(Context context, String path, String name, int version, boolean defaultCreate) {
        this(context, path, name, null, version, defaultCreate);
        this.DB_PATH = path.concat(File.separator).concat(name);
        this.DB_NAME = name;
    }

    public SDSqLiteOpen(Context context, String path, String name, CursorFactory factory, int version, boolean defaultCreate) {
        super(context, path, name, factory, version, defaultCreate);
        this.DB_PATH = path.concat(File.separator).concat(name);
        this.DB_NAME = name;
    }

    // 回调函数，第一次创建时才会调用此函数，创建一个数据库
    @Override
    public void onCreate(SQLiteDatabase db) {
        if (DbManager.dbConfig != null)
            DbManager.dbConfig.getDbListener().onCreate(db);
    }

    //数据升级(需要修改数据库版本号)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (DbManager.dbConfig != null)
            DbManager.dbConfig.getDbListener().onUpgrade(db, oldVersion, newVersion);
    }
}