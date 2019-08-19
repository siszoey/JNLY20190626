package com.titan.jnly.demo;

import com.lib.bandaid.arcruntime.util.TransformUtil;
import com.lib.bandaid.data.local.sqlite.core.builder.SqlBuilder;
import com.lib.bandaid.utils.PyUtil;
import com.titan.jnly.vector.bean.District;
import com.titan.jnly.vector.bean.TreeMode;
import com.titan.jnly.vector.bean.WorkSequence;
import com.titan.jnly.vector.enums.DataStatus;

public class Test {

    public static void main(String[] args) {
        String result = "Run Main";
        System.out.println(result);

        String sql = SqlBuilder.getTableBuildingSQL(new TreeMode());
        System.out.println(sql);

        DataStatus dataStatus = DataStatus.getEnum((short) 0);
        System.out.println(dataStatus);

        dataStatus = DataStatus.getEnum("远程同步");
        System.out.println(dataStatus);


        String lon2 = TransformUtil._10To60_len2("117.228611");
        String lat2 = TransformUtil._10To60_len2("31.794868");

        String lon = TransformUtil._10To60("117.228611");
        String lat = TransformUtil._10To60("31.794868");
        System.out.println(1122);

        double lond = TransformUtil._60To10(lon);
        double latd = TransformUtil._60To10(lat);

        System.out.println(1122);
        WorkSequence workSequence = new WorkSequence();
        Integer num = workSequence.sequence2Num("1908150101006");
        System.out.println(num);


       String ss = PyUtil.getInstance().convert2PyHead("测QA试,一下哈。喽qq123你好");
        System.out.println(ss);
    }


}
