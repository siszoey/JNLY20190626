package com.lib.bandaid.system.crash.bean;
import com.lib.bandaid.data.local.sqlite.core.annotation.Column;
import com.lib.bandaid.data.local.sqlite.core.annotation.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zy on 2019/4/24.
 */
@Table(name = "tb_crash_info")
public class CrashInfo implements Serializable {

    @Column(type = "char(32)", isPKey = true)
    private String uuid;

    @Column(type = "varchar(255)", isPKey = true)
    private String appKey;

    @Column(type = "varchar(255)")
    private String appName;

    @Column(type = "varchar(255)")
    private String versionName;

    @Column
    private Integer versionCode;

    @Column
    private String crashInfo;

    @Column
    private Date crashTime;

    public String reportUrl;


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    public String getCrashInfo() {
        return crashInfo;
    }

    public void setCrashInfo(String crashInfo) {
        this.crashInfo = crashInfo;
    }

    public Date getCrashTime() {
        return crashTime;
    }

    public void setCrashTime(Date crashTime) {
        this.crashTime = crashTime;
    }

    @Override
    public String toString() {
        return "CrashInfo{" +
                "uuid='" + uuid + '\'' +
                ", appKey='" + appKey + '\'' +
                ", appName='" + appName + '\'' +
                ", versionName='" + versionName + '\'' +
                ", versionCode=" + versionCode +
                ", crashInfo='" + crashInfo + '\'' +
                ", crashTime=" + crashTime +
                '}';
    }
}
