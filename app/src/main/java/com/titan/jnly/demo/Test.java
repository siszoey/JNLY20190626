package com.titan.jnly.demo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lib.bandaid.arcruntime.util.CustomUtil;
import com.lib.bandaid.data.local.sqlite.core.builder.SqlBuilder;
import com.lib.bandaid.util.PyUtil;
import com.titan.jnly.patrolv1.bean.PatrolTask;
import com.titan.jnly.vector.bean.TreeMode;
import com.titan.jnly.vector.bean.WorkSequence;
import com.titan.jnly.vector.enums.DataStatus;

public class Test {

    public static void main(String[] args) {




        Gson gson = new GsonBuilder()
                //.setDateFormat("yyyy-MM-dd HH:mm:ss")
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
        String json = "{\"Id\":\"29737974-376b-4548-b1cf-2a684e9ac7f5\",\"XIAN_N\":\"历下区\",\"RWH\":\"37010200043695\",\"DZBQH\":\"37010200043\",\"GSBH\":null,\"SZZWM\":\"白蜡\",\"SZK\":null,\"TXSJ\":\"2019-12-10T17:13:57\",\"XCY\":\"巡查员1\"}";
        PatrolTask patrolTask = gson.fromJson(json, PatrolTask.class);
        System.out.println(patrolTask);

/*
        String result = "Run Main";
        System.out.println(result);

        String sql = SqlBuilder.getTableBuildingSQL(new TreeMode());
        System.out.println(sql);

        DataStatus dataStatus = DataStatus.getEnum((short) 0);
        System.out.println(dataStatus);

        dataStatus = DataStatus.getEnum("远程同步");
        System.out.println(dataStatus);


        String lon2 = CustomUtil._10To60_len2("117.228611");
        String lat2 = CustomUtil._10To60_len2("31.794868");

        String lon = CustomUtil._10To60("117.228611");
        String lat = CustomUtil._10To60("31.794868");

        double lond = CustomUtil._60To10(lon);
        double latd = CustomUtil._60To10(lat);

        System.out.println(1122);
        WorkSequence workSequence = new WorkSequence();
        Integer num = workSequence.sequence2Num("1908150101006");
        System.out.println(num);


       String ss = PyUtil.getInstance().convert2PyHead("测QA试,一下哈。喽qq123你好");
        System.out.println(ss);*/
    }


}
