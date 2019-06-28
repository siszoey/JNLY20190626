package com.titan.jnly.system;

import com.lib.bandaid.app.BaseApp;
import com.lib.bandaid.utils.CacheUtil;
import com.titan.jnly.login.bean.User;

/**
 * app全局常量存储
 */
public final class Constant {

    private final static String KEY_USER = "KEY_USER";

    private static CacheUtil cacheUtil = CacheUtil.get(BaseApp.baseApp);

    public static void putUser(User user) {
        cacheUtil.put(KEY_USER, user);
    }

    public static void delUser() {
        cacheUtil.remove(KEY_USER);
    }

    public static User getUser() {
        return cacheUtil.getAsT(KEY_USER);
    }
}
