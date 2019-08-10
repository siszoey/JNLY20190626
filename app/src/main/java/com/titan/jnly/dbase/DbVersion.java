package com.titan.jnly.dbase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.lib.bandaid.data.local.sqlite.config.DbConfig;
import com.lib.bandaid.data.local.sqlite.core.builder.SqlBuilder;
import com.lib.bandaid.service.bean.Loc;
import com.titan.jnly.Config;
import com.titan.jnly.login.bean.UserInfo;
import com.titan.jnly.vector.bean.TreeMode;
import com.titan.jnly.vector.bean.WorkSequence;


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
        if (db.getPath().equals(privateDbPath())) {
            //sql = SqlBuilder.getTableBuildingSQL(DownInfo.class);
            //db.execSQL(sql);
        } else if (db.getPath().equals(defaultDbPath())) {
            sql = SqlBuilder.getTableBuildingSQL(new Loc());
            db.execSQL(sql);
            sql = SqlBuilder.getTableBuildingSQL(new UserInfo());
            db.execSQL(sql);
            sql = SqlBuilder.getTableBuildingSQL(new WorkSequence());
            db.execSQL(sql);
        } else {

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (db.getPath().equals(privateDbPath())) {

        } else if (db.getPath().equals(defaultDbPath())) {
           /* String sql = SqlBuilder.getTableBuildingSQL(Loc.class);
            db.execSQL(sql);*/
        } else {

        }
    }

    /**
     * root后才能看到的目录
     *
     * @return
     */
    @Override
    public String privateDbName() {
        return "system.db";
    }

    /**
     * 指定一个目录
     *
     * @return
     */
    @Override
    public String defaultDbPath() {
        return Config.APP_DB_PATH;
    }

    /**
     * 使用时才会指定的目录
     *
     * @return
     */
    @Override
    public int version() {
        return 1;
    }

    @Override
    public int defaultVersion() {
        return 1;
    }

    @Override
    public int privateVersion() {
        return 1;
    }
}
