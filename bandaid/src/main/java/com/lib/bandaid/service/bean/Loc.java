package com.lib.bandaid.service.bean;

import com.lib.bandaid.data.local.sqlite.core.annotation.Column;
import com.lib.bandaid.data.local.sqlite.core.annotation.Table;
import com.lib.bandaid.data.local.sqlite.utils.UUIDTool;

import java.io.Serializable;
import java.util.Date;

@Table(name = "TB_LOCATION")
public class Loc implements Serializable {

    public static String USER_ID;

    public Loc() {
        this.id = UUIDTool.get32UUID();
        this.dateTime = new Date();
        this.userId = USER_ID;
    }

    @Column(name = "F_ID", alias = "UUID主键", isPKey = true, type = "varchar(32)")
    private String id;

    @Column(name = "F_TRACK_ID", alias = "轨迹ID", type = "varchar(32)")
    private String trackId;

    @Column(name = "F_USER_ID", alias = "用户ID", type = "varchar(32)")
    private String userId;

    @Column(name = "F_LNG", alias = "经度", type = "varchar(12)")
    private String lng;

    @Column(name = "F_LAT", alias = "纬度", type = "varchar(12)")
    private String lat;

    @Column(name = "F_TIME", alias = "记录时间")
    private Date dateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Loc{" +
                "id='" + id + '\'' +
                ", trackId='" + trackId + '\'' +
                ", userId='" + userId + '\'' +
                ", lng='" + lng + '\'' +
                ", lat='" + lat + '\'' +
                ", dateTime=" + dateTime +
                '}';
    }
}
