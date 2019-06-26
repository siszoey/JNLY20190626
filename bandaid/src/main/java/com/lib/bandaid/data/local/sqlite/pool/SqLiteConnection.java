package com.lib.bandaid.data.local.sqlite.pool;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lib.bandaid.data.local.sqlite.utils.SDSqLiteOpen;

import java.io.File;

/**
 * Created by zy on 2018/1/2.
 * 数据库连接包装
 */

public class SqLiteConnection {

    private long threadID;//线程Id
    private long createTime;// 时间戳
    private SDSqLiteOpen sqLiteOpen;//线程id
    private SQLiteDatabase connection;// 链接对象
    private String path;

    public SqLiteConnection(Context context, String path, boolean defaultCreate) {
        this.createTime = System.currentTimeMillis();
        this.threadID = Thread.currentThread().getId();
        this.path = path;
        File file = new File(path);
        sqLiteOpen = new SDSqLiteOpen(context, file.getParent(), file.getName(), defaultCreate);
        /**
         * 读写暂未分离
         */
        if (sqLiteOpen != null) {
            if (connection == null) {
                connection = sqLiteOpen.getWritableDatabase();
            } else {
                if (!connection.isOpen()) {
                    connection = sqLiteOpen.getWritableDatabase();
                }
            }
        }
    }

    public SqLiteConnection(Context context, String path, int version, boolean defaultCreate) {
        this.createTime = System.currentTimeMillis();
        this.threadID = Thread.currentThread().getId();
        this.path = path;
        File file = new File(path);
        sqLiteOpen = new SDSqLiteOpen(context, file.getParent(), file.getName(), version, defaultCreate);
        /**
         * 读写暂未分离
         */
        if (sqLiteOpen != null) {
            if (connection == null) {
                connection = sqLiteOpen.getWritableDatabase();
            } else {
                if (!connection.isOpen()) {
                    connection = sqLiteOpen.getWritableDatabase();
                }
            }
        }
    }


    public long getCreateTime() {
        return createTime;
    }

    public String getPath() {
        return path;
    }

    public long getThreadID() {
        return threadID;
    }

    public void execSQL(String sql) {
        connection.execSQL(sql);
    }


    public Cursor rawQuery(String sql) {
        return connection.rawQuery(sql, null);
    }

    /**
     * 是否被代理
     *
     * @return
     */
    public boolean isProxy() {
        return isProxy;
    }

    //===========================动态代理拥有最终解释权===============================

    private boolean isProxy = false;


    public void beginTransaction() {
        if (!isProxy) {
            connection.beginTransaction();
        }
    }

    public void setTransactionSuccessful() {
        if (!isProxy) {
            connection.setTransactionSuccessful();
        }
    }

    public void endTransaction() {
        if (!isProxy) {
            connection.endTransaction();
        }
    }

    public void close() {
        if (connection != null && connection.isOpen()) {
            if (!isProxy) {
                connection.close();
            }
        }
    }


    public void finalBeginTransaction() {
        //System.out.println("finalBeginTransaction:1");
        if (connection != null && connection.isOpen() && !connection.inTransaction()) {
            connection.beginTransaction();
            isProxy = true;
            //System.out.println("finalBeginTransaction:2");
        }
    }

    public void finalSetTransactionSuccessful() {
        //System.out.println("finalSetTransactionSuccessful:1");
        if (connection != null && connection.isOpen() && connection.inTransaction()) {
            connection.setTransactionSuccessful();
            //System.out.println("finalSetTransactionSuccessful:2");
        }
    }

    public void finalEndTransaction() {
        //System.out.println("finalEndTransaction:1");
        if (connection != null && connection.isOpen() && connection.inTransaction()) {
            connection.endTransaction();
            //System.out.println("finalEndTransaction:2");
        }
    }

    public void finalClose() {
        if (connection != null && connection.isOpen()) {
            connection.close();
            sqLiteOpen.close();
            connection = null;
            sqLiteOpen = null;
            //System.out.println("connection.close()||sqLiteOpen.close()");
        }
    }
}
