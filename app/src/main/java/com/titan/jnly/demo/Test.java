package com.titan.jnly.demo;

import com.lib.bandaid.data.local.sqlite.core.builder.SqlBuilder;
import com.titan.jnly.vector.bean.District;

public class Test {

    public static void main(String[] args) {
        String result = "Run Main";
        System.out.println(result);

        String sql = SqlBuilder.getTableBuildingSQL(new District());
        System.out.println(sql);
    }


}
