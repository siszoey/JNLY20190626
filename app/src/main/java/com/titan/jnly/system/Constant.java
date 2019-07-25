package com.titan.jnly.system;

import android.content.Context;
import android.location.Location;

import com.lib.bandaid.app.BaseApp;
import com.lib.bandaid.rw.file.xml.IoXml;
import com.lib.bandaid.service.bean.Loc;
import com.lib.bandaid.utils.CacheUtil;
import com.lib.bandaid.widget.easyui.convert.Resolution;
import com.lib.bandaid.widget.easyui.xml.EasyUiXml;
import com.titan.jnly.Config;
import com.titan.jnly.login.bean.User;

import java.util.ArrayList;
import java.util.List;

/**
 * app全局常量存储
 */
public final class Constant {

    private final static String KEY_USER = "KEY_USER";

    private static CacheUtil cacheUtil = CacheUtil.get(BaseApp.baseApp);

    private static List<EasyUiXml> easyUiXmls = new ArrayList<>();

    public static Location location;


    public static void putUser(User user) {
        Loc.USER_ID = user.getName();
        cacheUtil.put(KEY_USER, user);
    }

    public static void delUser() {
        cacheUtil.remove(KEY_USER);
    }

    public static User getUser() {
        return cacheUtil.getAsT(KEY_USER);
    }

    public static EasyUiXml getEasyUiXmlByName(Context context, String name) {
        if (easyUiXmls.size() == 0) {
            initEasyUiXml(context, Config.GEO_TB_MODULE);
        }
        for (EasyUiXml easyUiXml : easyUiXmls) {
            if (easyUiXml.getName().equals(name)) return easyUiXml;
        }
        return null;
    }

    public static void initEasyUiXml(Context context, String... paths) {
        if (paths == null) return;
        EasyUiXml easyUiXml;
        for (String path : paths) {
            easyUiXml = IoXml.readXmlFromAssets(context, EasyUiXml.class, path);
            if (easyUiXml == null) continue;
            easyUiXmls.add(easyUiXml);
        }
    }
}
