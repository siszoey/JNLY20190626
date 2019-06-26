package com.titan.jnly.dbase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.lib.bandaid.data.local.sqlite.config.DbConfig;
import com.lib.bandaid.data.local.sqlite.core.builder.SqlBuilder;
import com.titan.jnly.Config;


/**
 * Created by zy on 2019/4/19.
 */

public class DbVersion extends DbConfig {


    public DbVersion(Context context) {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql;
        /*if (db.getPath().equals(privateDbPath())) {
            sql = SqlBuilder.getTableBuildingSQL(DownInfo.class);
            db.execSQL(sql);
        } else if (db.getPath().equals(defaultDbPath())) {
            sql = SqlBuilder.getTableBuildingSQL(new DbTest1());
            db.execSQL(sql);
            sql = SqlBuilder.getTableBuildingSQL(new DbTest2());
            db.execSQL(sql);
        }*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (db.getPath().equals(privateDbPath())) {

        } else if (db.getPath().equals(defaultDbPath())) {
           /* String sql = SqlBuilder.getDropSql(DownInfo.class);
            db.execSQL(sql);

            sql = SqlBuilder.getTableBuildingSQL(DownInfo.class);
            db.execSQL(sql);*/
        }
    }

    @Override
    public String privateDbName() {
        return "system.db";
    }

    @Override
    public String defaultDbPath() {
        return Config.APP_DB_PATH;
    }

    @Override
    public int version() {
        return 1;
    }

    @Override
    public int defaultVersion() {
        return 4;
    }

    @Override
    public int privateVersion() {
        return 1;
    }
}
