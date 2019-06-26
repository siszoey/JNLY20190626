package com.lib.bandaid.service.imp;

import android.location.Location;

/**
 * Created by zy on 2018/4/1.
 */

public class ServiceLocation {
    private ESignal eSignal;
    private Location location;
    private Integer satelliteCount;
    private Integer validSatelliteCount;

    public ESignal geteSignal() {
        return eSignal;
    }

    public void seteSignal(ESignal eSignal) {
        this.eSignal = eSignal;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Integer getSatelliteCount() {
        return satelliteCount;
    }

    public void setSatelliteCount(Integer satelliteCount) {
        this.satelliteCount = satelliteCount;
    }

    public Integer getValidSatelliteCount() {
        return validSatelliteCount;
    }

    public void setValidSatelliteCount(Integer validSatelliteCount) {
        this.validSatelliteCount = validSatelliteCount;
    }

    public enum ESignal {
        HIGH,
        MIDDLE,
        LOW;
    }
}
