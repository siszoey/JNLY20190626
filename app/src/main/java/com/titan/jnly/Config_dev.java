package com.titan.jnly;

public class Config_dev {
    public static String APP_PATH_NAME = "jnly_dev";
    public static String APP_DB_NAME = "jnly_dev.db";


    public interface BASE_URL {
        String MON = "http://gykj123.cn:9033/";
        String TUE = "Tue";
        String WED = "Wed";
        String THU = "Thu";
        String FRI = "Fri";
        String SAT = "Sat";
        String SUN = "Sun";
    }


    public final static String APP_ARC_MAP_SERVICE = "http://222.85.147.144:6080/arcgis/rest/services/GYLY_BASEDATA/LDLJXB/MapServer";
    public final static String APP_ARC_MAP_SERVICE_2015_SS = "http://222.85.147.144:6080/arcgis/rest/services/GYLY_BASEDATA/ED_QXFQ_2015BZ/MapServer";
}
