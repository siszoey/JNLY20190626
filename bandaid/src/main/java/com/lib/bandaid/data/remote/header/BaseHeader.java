package com.lib.bandaid.data.remote.header;

import com.google.gson.Gson;
import com.lib.bandaid.app.BaseApp;
import com.lib.bandaid.util.AppUtil;
import com.lib.bandaid.util.GsonFactory;
import com.lib.bandaid.util.SystemUtil;

import java.io.Serializable;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;

/**
 * Created by zy on 2018/8/15.
 */

public class BaseHeader implements Serializable {

    /**
     * app名称
     */
    private String appName = AppUtil.getAppName(BaseApp.baseApp);
    /**
     * app版本名称
     */
    private String appVersionName = AppUtil.getApkVersionName(BaseApp.baseApp);
    /**
     * app版本号
     */
    private String appVersionCode = AppUtil.getApkVersionCode(BaseApp.baseApp) + "";
    /**
     * 手机设备号
     */
    private String imei = SystemUtil.getIMEI(BaseApp.baseApp);
    /**
     * 手机设备号
     */
    private String systemModel = SystemUtil.getSystemModel();
    /**
     * 请求时间（移动端）
     */
    private Date dateTime = new Date();

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVersionName() {
        return appVersionName;
    }

    public void setAppVersionName(String appVersionName) {
        this.appVersionName = appVersionName;
    }

    public String getAppVersionCode() {
        return appVersionCode;
    }

    public void setAppVersionCode(String appVersionCode) {
        this.appVersionCode = appVersionCode;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getSystemModel() {
        return systemModel;
    }

    public void setSystemModel(String systemModel) {
        this.systemModel = systemModel;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public static String encode(Object o) {
        if (o == null) return null;
        Gson gson = GsonFactory.getFactory().getComGson();
        String json = gson.toJson(o);
        try {
            return URLEncoder.encode(json, "UTF-8");
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T decode(String o, Class clazz) {
        try {
            Gson gson = GsonFactory.getFactory().getComGson();
            String json = URLDecoder.decode(o, "UTF-8");
            return (T) gson.fromJson(json, clazz);
        } catch (Exception e) {
            return null;
        }
    }
}
