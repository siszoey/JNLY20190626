package com.titan.jnly.vector.util;

import com.lib.bandaid.data.local.sqlite.proxy.transaction.DbManager;
import com.lib.bandaid.utils.DateUtil;
import com.titan.jnly.Config;
import com.titan.jnly.login.bean.UserInfo;
import com.titan.jnly.system.Constant;
import com.titan.jnly.vector.bean.TreeMode;
import com.titan.jnly.vector.bean.WorkSequence;

import java.util.Date;
import java.util.List;

/**
 * 回归模型计算树龄
 */
public final class DbEasyUtil {

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
            //(0,0) (a,b) (x,y)
            //  a/x = b/y;y=bx/a
            double year;
            if ("1".equals(flag)) {
                year = min.getYear() * diam / min.getDiamHill();
            } else {
                year = min.getYear() * diam / min.getDiamPlain();
            }
            return ((int) year / 100) * 100;
            //return min.getYear();
        } else {
            //(0,0) (a,b) (x,y)
            //  a/x = b/y;y=bx/a
            double year;
            if ("1".equals(flag)) {
                year = max.getYear() * diam / max.getDiamHill();
            } else {
                year = max.getYear() * diam / max.getDiamPlain();
            }
            if (year < 30) {
                return 20;
            } else if (year >= 30 && year < 50) {
                return 40;
            } else if (year >= 50 && year < 70) {
                return 60;
            } else if (year >= 70) {
                return 80;
            }
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

    public static String getWorkSequence() {
        UserInfo userInfo = Constant.getUserInfo();
        if (userInfo == null) return null;
        String userName = userInfo.getUserName();
        if (userInfo.getUserGroup() == null) return null;
        String sql = "select * from TB_Work_Sequence where userName = '" + userName + "' and date(dateTime) = date('now') ORDER BY num DESC LIMIT 1 OFFSET 0";
        System.out.println(">>>>>:   " + sql);
        WorkSequence workSequence = (WorkSequence) DbManager.createDefault().getTBySql(WorkSequence.class, sql, true);
        //没有当天数据
        if (workSequence == null) {
            workSequence = new WorkSequence();
            workSequence.setDateTime(new Date());
            workSequence.setUserName(userName);
            workSequence.setNum(1);
            String prefix = DateUtil.curTimeFormat("yyMMdd");
            String group = userInfo.getUserGroup();
            String suffix = workSequence.getNumFormat();
            workSequence.setSequence(prefix + group + suffix);
        } else {
            workSequence.setNum(workSequence.getNum() + 1);
            workSequence.setDateTime(new Date());
            String prefix = DateUtil.curTimeFormat("yyMMdd");
            String group = userInfo.getUserGroup();
            String suffix = workSequence.getNumFormat();
            workSequence.setSequence(prefix + group + suffix);
        }
        boolean flag = DbManager.createDefault().saveOrUpdate(workSequence);
        if (flag) return workSequence.getSequence();
        else return null;
    }

}
