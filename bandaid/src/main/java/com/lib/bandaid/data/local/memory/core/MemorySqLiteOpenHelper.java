package com.lib.bandaid.data.local.memory.core;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by zy on 2018/4/27.
 */

public class MemorySqLiteOpenHelper {

    private void init() {
        SQLiteDatabase database = SQLiteDatabase.create(null);
    }

}
