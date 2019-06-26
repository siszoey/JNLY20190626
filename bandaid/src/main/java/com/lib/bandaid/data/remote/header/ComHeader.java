package com.lib.bandaid.data.remote.header;


/**
 * Created by zy on 2018/8/15.
 */

public class ComHeader extends BaseHeader {

    public final static String HEAD_FLAG = "ComHeader";

    private String userName;

    private String areaCode;

    private String areaName;

    private String companyCode;

    private String companyName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
