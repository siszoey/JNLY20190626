package com.titan.jnly.vector.util;

import com.lib.bandaid.data.local.sqlite.proxy.transaction.DbManager;
import com.titan.jnly.vector.bean.TreeMode;

import java.util.List;

/**
 * 回归模型计算树龄
 */
public final class TreeModeUtil {

    /**
     * @param name 树种名称
     * @param diam 树的胸径
     * @return
     */
    public static int computeTreeAge(String name, Double diam) {
        String sql = "select * from TB_TreeMode where f_name = '" + name + "' and f_diam <= " + diam + " order by f_diam desc limit 1 OFFSET 0";
        TreeMode min = (TreeMode) DbManager.createDefault().getTBySql(TreeMode.class, sql, true);

        sql = "select * from TB_TreeMode where f_name = '" + name + "' and f_diam >= " + diam + " order by f_diam asc limit 1 OFFSET 0";
        TreeMode max = (TreeMode) DbManager.createDefault().getTBySql(TreeMode.class, sql, true);

        if (min != null && max != null) {
            Double age = (diam - min.getDiam()) * (max.getYear() - min.getYear()) / (max.getDiam() - min.getDiam()) + min.getYear();
            return age.intValue();
        } else if (min != null && max == null) {
            return 10000;
        } else {
            return -10000;
        }

    }

    /**
     * @param name  树种名称
     * @param cycle 树的周长
     * @return
     */
    public static int computeTreeAgeByCycle(String name, Double cycle) {
        return computeTreeAge(name, cycle / Math.PI);
    }

}
