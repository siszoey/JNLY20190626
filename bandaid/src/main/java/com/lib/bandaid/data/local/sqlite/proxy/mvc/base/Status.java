package com.lib.bandaid.data.local.sqlite.proxy.mvc.base;

import android.content.Context;
import android.database.Cursor;

import com.lib.bandaid.data.local.sqlite.pool.SessionFactory;
import com.lib.bandaid.data.local.sqlite.pool.SqLiteConnection;

import java.io.File;
import java.util.List;

/**
 * Created by zy on 2018/1/2.
 */

public abstract class Status<T> implements IStatus<T> {

    protected Context context;
    protected String dbPath;
    protected int version;

    public Status(Context context, String dbPath, int version) {
        this.context = context;
        this.dbPath = dbPath;
        this.version = version;
    }

    @Override
    public void onReady() {
        File file = new File(dbPath);
        if (!file.exists()) new Throwable("数据库不存在！");
    }


    @Override
    public void end() {

    }

    @Override
    public void cancel() {

    }

    @Override
    public void onDestroy() {

    }

    public boolean exec(String sql) {
        onReady();
        boolean flag = false;
        SqLiteConnection conn = null;
        try {
            //获得数据库连接
            conn = SessionFactory.getCurrentSession(context, dbPath, version);
            conn.beginTransaction();//开启事物
            conn.execSQL(sql);
            conn.setTransactionSuccessful();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
            throw new UnsupportedOperationException(e);
        } finally {
            conn.endTransaction();
            SessionFactory.close(conn, null);
        }
        return flag;
    }

    public boolean exec(List<String> sqls) {
        onReady();
        boolean flag = false;
        SqLiteConnection conn = null;
        try {
            //获得数据库连接
            conn = SessionFactory.getCurrentSession(context, dbPath, version);
            conn.beginTransaction();//开启事物
            for (int i = 0; i < sqls.size(); i++) {
                String sql = sqls.get(i);
                conn.execSQL(sql);
            }
            conn.setTransactionSuccessful();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnsupportedOperationException();
        } finally {
            conn.endTransaction();
            SessionFactory.close(conn, null);
        }
        return flag;
    }

    public Cursor query(String sql) {
        onReady();
        SqLiteConnection conn = null;
        Cursor cursor = null;
        try {
            //获得数据库连接
            conn = SessionFactory.getCurrentSession(context, dbPath, version);
            //开启事物
            conn.beginTransaction();
            cursor = conn.rawQuery(sql);
            conn.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnsupportedOperationException();
        } finally {
            conn.endTransaction();
            SessionFactory.close(conn, null);
        }
        return cursor;
    }


    public boolean exist(String sql) {
        onReady();
        boolean flag = false;
        SqLiteConnection conn = null;
        Cursor cursor = null;
        try {
            //获得数据库连接
            conn = SessionFactory.getCurrentSession(context, dbPath, version);
            conn.beginTransaction();//开启事物
            cursor = query(sql);
            int i = 0;
            while (cursor.moveToNext()) {
                //i = cursor.getInt(0);
                i++;
                break;
            }
            if (i > 0) {
                flag = true;
            } else {
                flag = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
            throw new UnsupportedOperationException();
        } finally {
            conn.endTransaction();
            SessionFactory.close(conn, cursor);
        }
        return flag;
    }
}
