package com.lib.bandaid.data.local.sqlite.proxy.transaction;


import com.lib.bandaid.data.local.sqlite.config.DbConfig;
import com.lib.bandaid.data.local.sqlite.pool.SessionFactory;
import com.lib.bandaid.data.local.sqlite.pool.SqLiteConnection;

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by zy on 2018/1/2.
 */

final class TransactionHandler implements InvocationHandler {

    private DbConfig dbConfig;
    //要处理的对象，声明为Object类型是为了通用性
    private Object targetObject;
    private String dbPath;
    private int version;

    //动态生成方法被处理过后的对象
    public Object newProxyInstance(Object targetObject) {
        this.targetObject = targetObject;
        //参数1：类的加载器,参数2：确定继承类的接口
        return Proxy.newProxyInstance(targetObject.getClass().getClassLoader(), targetObject.getClass().getInterfaces(), this);
    }

    /**
     * 非动态路径
     *
     * @param dbConfig
     * @param isDefault
     */
    public TransactionHandler(DbConfig dbConfig, boolean isDefault) {
        this.dbConfig = dbConfig;
        File file;
        if (isDefault) {
            this.dbPath = dbConfig.privateDbPath();
            this.version = dbConfig.privateVersion();
        } else {
            this.dbPath = dbConfig.defaultDbPath();
            this.version = dbConfig.defaultVersion();
        }
        file = new File(dbPath);
        if (!file.exists()) {
            file = file.getParentFile();
            if (!file.exists()) {
                System.out.println("数据库所属路径不存在！,创建：" + file.getAbsolutePath());
                if (file.mkdirs()) {
                    System.out.println("数据库所属路径创建成功！");
                } else {
                    System.out.println("数据库所属路径创建失败！");
                }
            }
        }
    }

    /**
     * 动态路径
     *
     * @param dbConfig
     * @param dbPath
     */
    public TransactionHandler(DbConfig dbConfig, String dbPath) {
        this.dbConfig = dbConfig;
        this.dbPath = dbPath;
        this.version = dbConfig.version();
        File file = new File(dbPath);
        if (!file.exists()) {
            file = file.getParentFile();
            if (!file.exists()) {
                System.out.println("数据库所属路径不存在！,创建：" + file.getAbsolutePath());
                if (file.mkdirs()) {
                    System.out.println("数据库所属路径创建成功！");
                } else {
                    System.out.println("数据库所属路径创建失败！");
                }
            }
        }
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        SqLiteConnection sqLiteConnection = null;
        Object result = null;
        boolean isTransaction = false;
        try {
            //取得Connection
            sqLiteConnection = SessionFactory.getLocalSession(dbConfig.getContext(), dbPath, version);
            //判断方法，开启事物
            String methodName = method.getName().toLowerCase();
            if (dbConfig != null) {
                for (int i = 0; i < dbConfig.transaction().length; i++) {
                    if (methodName.contains(dbConfig.transaction()[i])) {
                        // 开启事务
                        isTransaction = true;
                    }
                }
            }
            if (isTransaction) {
                sqLiteConnection.finalBeginTransaction();
            }
            //调用目标对象的业务逻辑方法
            result = method.invoke(targetObject, args);
            //提交事务
            if (isTransaction) {
                sqLiteConnection.finalSetTransactionSuccessful();
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        } finally {
            if (isTransaction) {
                sqLiteConnection.finalEndTransaction();
            }
            SessionFactory.finalClose(sqLiteConnection);
        }
        return result;
    }
}
