package com.titan.jnly;

public class Config_dev {

    public static String APP_PATH_DIR = "jnly";
    public static String APP_DB_NAME = "jnly.db";
    public static String APP_DIC_DB_NAME = "dic.db";


    /**
     * 测试服务地址
     */
    public interface BASE_URL {
        String BaseUrl_Mock = "http://127.0.0.1:8080/";
        String BaseUrl_1 = "http://101.201.54.143:9003/";
        String BaseFileService = "http://101.201.54.143:9088";
        String Sync_Dic = "http://119.164.253.207:8090/DicData/dic.db";
        //图片浏览更路径
        String ImgScanService = "http://119.164.253.207:8092/api/FileServer/download/";

        String[] Fea_MapService = new String[]{
                "http://119.164.253.207:6080/arcgis/rest/services/JNGSMM/JNGSMM_2019/MapServer",
                "http://119.164.253.207:6080/arcgis/rest/services/JNGSMM/JNGSQ_2019/MapServer"
        };
    }
}
