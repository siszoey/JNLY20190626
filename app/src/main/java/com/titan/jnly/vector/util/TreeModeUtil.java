package com.titan.jnly.vector.util;

import com.lib.bandaid.data.local.sqlite.proxy.transaction.DbManager;
import com.titan.jnly.Config;
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
    public static int computeTreeAge(String name, Double diam, String flag) {
        if (flag == null) return -1;
        TreeMode min, max;
        if ("1".equals(flag)) {
            String sql = "select * from TB_TreeMode where f_name = '" + name + "' and f_diam_hill <= " + diam + " order by f_diam_hill desc limit 1 OFFSET 0";
            System.out.println(sql);
            min = (TreeMode) DbManager.create(Config.APP_DIC_DB_PATH).getTBySql(TreeMode.class, sql, true);
            sql = "select * from TB_TreeMode where f_name = '" + name + "' and f_diam_hill >= " + diam + " order by f_diam_hill asc limit 1 OFFSET 0";
            System.out.println(sql);
            max = (TreeMode) DbManager.create(Config.APP_DIC_DB_PATH).getTBySql(TreeMode.class, sql, true);
        } else {
            String sql = "select * from TB_TreeMode where f_name = '" + name + "' and f_diam_plain <= " + diam + " order by f_diam_plain desc limit 1 OFFSET 0";
            System.out.println(sql);
            min = (TreeMode) DbManager.create(Config.APP_DIC_DB_PATH).getTBySql(TreeMode.class, sql, true);
            sql = "select * from TB_TreeMode where f_name = '" + name + "' and f_diam_plain >= " + diam + " order by f_diam_plain asc limit 1 OFFSET 0";
            System.out.println(sql);
            max = (TreeMode) DbManager.create(Config.APP_DIC_DB_PATH).getTBySql(TreeMode.class, sql, true);
        }
        if (min != null && max != null) {
            /*Double age = (diam - min.getDiam()) * (max.getYear() - min.getYear()) / (max.getDiam() - min.getDiam()) + min.getYear();
            return age.intValue();*/
            //中位值计算
            double mid;
            if ("1".equals(flag)) {
                mid = (max.getDiamHill() + min.getDiamHill()) / 2;
                if (diam >= mid) return max.getYear();
                else return min.getYear();
            } else {
                mid = (max.getDiamPlain() + min.getDiamPlain()) / 2;
                if (diam >= mid) return max.getYear();
                else return min.getYear();
            }
        } else if (min != null && max == null) {
            return min.getYear();
        } else {
            return max.getYear();
        }

    }

    /**
     * @param name  树种名称
     * @param cycle 树的周长
     * @return
     */
    public static int computeTreeAgeByCycle(String name, Double cycle, String flag) {
        return computeTreeAge(name, cycle / Math.PI, flag);
    }

}
