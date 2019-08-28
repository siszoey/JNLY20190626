package com.titan.jnly;

public class Config_pro {

    public static String APP_PATH_DIR = "jnly";
    public static String APP_DB_NAME = "jnly.db";
    public static String APP_DIC_DB_NAME = "dic.db";


    public interface BASE_URL {
        String BaseUrl_1 = "http://119.164.253.207:8091/";
        String BaseFileService = "http://119.164.253.207:8092/";
        String Sync_Dic = "http://119.164.253.207:8090/DicData/dic.db";
        String[] Fea_MapService = new String[]{
                "http://119.164.253.207:6080/arcgis/rest/services/JNGSMM/JNGSMM_2019/MapServer",
                "http://119.164.253.207:6080/arcgis/rest/services/JNGSMM/JNGSQ_2019/MapServer"
        };
        String THU = "Thu";
        String FRI = "Fri";
        String SAT = "Sat";
        String SUN = "Sun";
    }
}
