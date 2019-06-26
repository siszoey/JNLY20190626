package com.lib.bandaid.data.local.sqlite.config;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by zy on 2019/4/19.
 */

public interface DbListener {
    public void onCreate(SQLiteDatabase db);

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
}
