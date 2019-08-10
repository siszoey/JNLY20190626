package com.titan.jnly.system;

import android.content.Context;
import android.location.Location;

import com.lib.bandaid.app.BaseApp;
import com.lib.bandaid.data.local.sqlite.proxy.transaction.DbManager;
import com.lib.bandaid.rw.file.xml.IoXml;
import com.lib.bandaid.service.bean.Loc;
import com.lib.bandaid.utils.CacheUtil;
import com.lib.bandaid.widget.easyui.convert.Resolution;
import com.lib.bandaid.widget.easyui.xml.EasyUiXml;
import com.titan.jnly.Config;
import com.titan.jnly.login.bean.User;
import com.titan.jnly.login.bean.UserInfo;
import com.titan.jnly.vector.bean.Species;

import java.util.ArrayList;
import java.util.List;

/**
 * app全局常量存储
 */
public final class Constant {

    private final static String KEY_USER = "KEY_USER";
    private final static String KEY_USER_INFO = "KEY_USER_INFO";

    private static CacheUtil cacheUtil = CacheUtil.get(BaseApp.baseApp);

    private static List<EasyUiXml> Constant_EasyUiXml = new ArrayList<>();
    private static List<Species> Constant_Species = new ArrayList<>();

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


    public static void putUserInfo(UserInfo userInfo) {
        cacheUtil.put(KEY_USER_INFO, userInfo);
    }


    public static void delUserInfo() {
        cacheUtil.remove(KEY_USER_INFO);
    }

    public static UserInfo getUserInfo() {
        return cacheUtil.getAsT(KEY_USER_INFO);
    }

    public static void initialize(Context context, String... paths) {
        initEasyUiXml(context, paths);
        initSpecies();
    }

    public static EasyUiXml getEasyUiXmlByName(Context context, String name) {
        if (Constant_EasyUiXml.size() == 0) {
            initEasyUiXml(context, Config.GEO_TB_MODULE);
        }
        for (EasyUiXml easyUiXml : Constant_EasyUiXml) {
            if (easyUiXml.getName().equals(name)) return easyUiXml;
        }
        return null;
    }

    private static void initEasyUiXml(Context context, String... paths) {
        if (paths == null) return;
        EasyUiXml easyUiXml;
        for (String path : paths) {
            easyUiXml = IoXml.readXmlFromAssets(context, EasyUiXml.class, path);
            if (easyUiXml == null) continue;
            Constant_EasyUiXml.add(easyUiXml);
        }
    }

    /**
     * 读取数据库树种
     */
    private static void initSpecies() {
        if (Constant_Species.size() == 0) {
            List<Species> list = DbManager.create(Config.APP_DIC_DB_PATH).getListTByWhere(Species.class, " where 1=1");
            if (list == null) return;
            Constant_Species.addAll(list);
        }
    }

    public static List<Species> getSpecies() {
        initSpecies();
        return Constant_Species;
    }

    public static Species getSpecies(String name) {
        initSpecies();
        Species species;
        for (int i = 0; i < Constant_Species.size(); i++) {
            species = Constant_Species.get(i);
            if (species.getSpecies().equals(name)) return species;
        }
        return null;
    }

    public static Species getSpeciesByCode(String code) {
        initSpecies();
        Species species;
        for (int i = 0; i < Constant_Species.size(); i++) {
            species = Constant_Species.get(i);
            if (species.getCode().equals(code)) return species;
        }
        return null;
    }
}
