package com.lib.bandaid.data.local.sqlite.pool;

import android.content.Context;
import android.database.Cursor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zy on 2018/3/19.
 */

public class SessionFactory {

    private static AtomicInteger atomicInteger;
    /**
     * 线程安全map
     */
    private static Map<SqLiteConnection, AtomicInteger> connections = new ConcurrentHashMap<>();

    /**
     * 代理开启数据库操作连接
     *
     * @param context
     * @param dbPath
     * @return
     */
    public static SqLiteConnection getLocalSession(Context context, String dbPath, int version) {
        long threadId = Thread.currentThread().getId();
        SqLiteConnection sqLiteConnection = null;
        AtomicInteger _atomicInteger;
        synchronized (connections) {
            if (connections != null) {
                for (SqLiteConnection s : connections.keySet()) {
                    if (s.getPath().equals(dbPath)) {  //s.getThreadID() == threadId &&
                        sqLiteConnection = s;
                        _atomicInteger = connections.get(sqLiteConnection);
                        _atomicInteger.incrementAndGet();
                        break;
                    }
                }
            }
            if (sqLiteConnection == null) {
                sqLiteConnection = new SqLiteConnection(context, dbPath, version, false);
                atomicInteger = new AtomicInteger();
                atomicInteger.incrementAndGet();
                connections.put(sqLiteConnection, atomicInteger);
            }
            return sqLiteConnection;
        }
    }

    /**
     * 获取链接
     *
     * @param context
     * @param dbPath
     * @return
     */
    public static SqLiteConnection getCurrentSession(Context context, String dbPath, int version) {
        long threadId = Thread.currentThread().getId();
        SqLiteConnection sqLiteConnection = null;
        AtomicInteger _atomicInteger;
        synchronized (connections) {
            if (connections != null) {
                for (SqLiteConnection s : connections.keySet()) {
                    if (s.getPath().equals(dbPath)) {  //s.getThreadID() == threadId &&
                        sqLiteConnection = s;
                        _atomicInteger = connections.get(sqLiteConnection);
                        _atomicInteger.incrementAndGet();
                        break;
                    }
                }
            }
            if (sqLiteConnection == null) {
                sqLiteConnection = new SqLiteConnection(context, dbPath, version, false);
                atomicInteger = new AtomicInteger();
                atomicInteger.incrementAndGet();
                connections.put(sqLiteConnection, atomicInteger);
            }
            System.out.println(dbPath + "|请求访问数：" + atomicInteger.get());
            return sqLiteConnection;
        }
    }

    //===============================被代理解释权============================

    public static void close(SqLiteConnection sqLiteConnection, Cursor cursor) {
        synchronized (connections) {
            int count = -1;
            if (connections != null && sqLiteConnection != null && connections.containsKey(sqLiteConnection)) {
                AtomicInteger atomicInteger = connections.get(sqLiteConnection);
                count = atomicInteger.decrementAndGet();
                System.out.println("数据库连接关闭：" + atomicInteger.get());
            }

            if (sqLiteConnection != null && !sqLiteConnection.isProxy()) {
                if (connections != null) {
                    if (connections.containsKey(sqLiteConnection)) {
                        if (count == 0) connections.remove(sqLiteConnection);
                    }
                }
            }
            if (count == 0 && sqLiteConnection != null) {
                sqLiteConnection.close();
                sqLiteConnection = null;
            }
        }
        if (cursor != null) {
            if (!cursor.isClosed()) {
                cursor.close();
            }
            cursor = null;
        }
    }

    //================================代理拥有最终解释权======================================

    public static void finalClose(SqLiteConnection sqLiteConnection) {
        synchronized (connections) {
            int count = -1;
            if (connections != null) {
                if (sqLiteConnection != null && connections.containsKey(sqLiteConnection)) {
                    AtomicInteger atomicInteger = connections.get(sqLiteConnection);
                    count = atomicInteger.decrementAndGet();
                    System.out.println("数据库连接最终关闭：" + atomicInteger.get());
                    if (count == 0) {
                        connections.remove(sqLiteConnection);
                    }
                }
            }
            if (count == 0 && sqLiteConnection != null) {
                sqLiteConnection.finalClose();
                sqLiteConnection = null;
            }
        }
    }
}
