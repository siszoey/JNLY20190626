package com.titan.jnly.vector.bean;

import com.lib.bandaid.data.local.sqlite.core.annotation.Column;
import com.lib.bandaid.data.local.sqlite.core.annotation.Table;

import java.io.Serializable;

@Table(name = "TB_District")
public class District implements Serializable {

    @Column(name = "f_code", isPKey = true, type = "varchar(12)")
    private String areaCode;

    @Column(name = "f_name", type = "varchar(100)")
    private String areaName;

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

    @Override
    public String toString() {
        return "District{" +
                "areaCode='" + areaCode + '\'' +
                ", areaName='" + areaName + '\'' +
                '}';
    }
}
