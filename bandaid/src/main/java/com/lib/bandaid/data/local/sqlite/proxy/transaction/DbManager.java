package com.lib.bandaid.data.local.sqlite.proxy.transaction;

import com.lib.bandaid.data.local.sqlite.config.DbConfig;
import com.lib.bandaid.data.local.sqlite.utils.ReflectUtil;
import com.lib.bandaid.data.local.sqlite.proxy.mvc.service.i.IMvcService;
import com.lib.bandaid.data.local.sqlite.proxy.mvc.service.s.MvcService;

/**
 * Created by zy on 2018/1/2.
 */

public final class DbManager {

    public static DbConfig dbConfig;

    public static <T> T create(Class clazz, String dbPath) {
        try {
            TransactionHandler transactionHandler = new TransactionHandler(dbConfig, dbPath);
            Object o = ReflectUtil.newInstance(clazz, dbConfig.getContext(), dbPath, dbConfig.version());
            return (T) transactionHandler.newProxyInstance(o);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 按默认config去创建
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T createDefault(Class clazz) {
        try {
            TransactionHandler transactionHandler = new TransactionHandler(dbConfig, false);
            Object o = ReflectUtil.newInstance(clazz, dbConfig.getContext(), dbConfig.defaultDbPath(), dbConfig.defaultVersion());
            return (T) transactionHandler.newProxyInstance(o);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 内部创建
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T createPrivate(Class clazz) {
        try {
            TransactionHandler transactionHandler = new TransactionHandler(dbConfig, true);
            Object o = ReflectUtil.newInstance(clazz, dbConfig.getContext(), dbConfig.privateDbPath(), dbConfig.privateVersion());
            return (T) transactionHandler.newProxyInstance(o);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static IMvcService create(String dbPath) {
        try {
            TransactionHandler transactionHandler = new TransactionHandler(dbConfig, dbPath);
            Object o = ReflectUtil.newInstance(MvcService.class, dbConfig.getContext(), dbPath, dbConfig.version());
            return (IMvcService) transactionHandler.newProxyInstance(o);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 按默认config去创建
     *
     * @return
     */
    public static IMvcService createDefault() {
        try {
            TransactionHandler transactionHandler = new TransactionHandler(dbConfig, false);
            Object o = ReflectUtil.newInstance(MvcService.class, dbConfig.getContext(), dbConfig.defaultDbPath(), dbConfig.defaultVersion());
            return (IMvcService) transactionHandler.newProxyInstance(o);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 内部创建
     *
     * @return
     */
    public static IMvcService createPrivate() {
        try {
            TransactionHandler transactionHandler = new TransactionHandler(dbConfig, true);
            Object o = ReflectUtil.newInstance(MvcService.class, dbConfig.getContext(), dbConfig.privateDbPath(), dbConfig.privateVersion());
            return (IMvcService) transactionHandler.newProxyInstance(o);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
