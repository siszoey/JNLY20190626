package com.lib.bandaid.data.local.sqlite.config;

import android.content.Context;

/**
 * Created by zy on 2019/4/18.
 */

public abstract class DbConfig implements DbListener {

    private Context context;

    public DbConfig(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public DbListener getDbListener() {
        return this;
    }

    /**
     * 私有文件夹下的数据库名称
     *
     * @return
     */
    public abstract String privateDbName();

    public String privateDbPath() {
        return context.getDatabasePath(privateDbName()).getPath();
    }

    /**
     * app共有文件夹下的数据库路径
     *
     * @return
     */
    public abstract String defaultDbPath();

    public abstract int version();

    public abstract int defaultVersion();

    public abstract int privateVersion();


    public String[] transaction() {
        return new String[]{"del", "insert", "save", "update"};
    }
}
