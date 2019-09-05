package com.titan.jnly.examine.util;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;

import java.util.Map;

public final class ExamineUtil {

    public static Poi mapConvertPoi(Map map) {
        String code = (String) map.get("DZBQH");
        String name = (String) map.get("SZZWM");
        double lat = (double) map.get("LAT");
        double lon = (double) map.get("LON");
        return new Poi(name + "(" + code + ")", new LatLng(lat, lon), null);
    }

}
