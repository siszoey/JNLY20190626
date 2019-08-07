package com.titan.jnly.demo;

import com.lib.bandaid.data.local.sqlite.core.builder.SqlBuilder;
import com.titan.jnly.vector.bean.District;
import com.titan.jnly.vector.bean.TreeMode;
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



    }


}
