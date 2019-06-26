package com.lib.bandaid.data.local.sqlite.proxy.mvc.dao.d;

import android.content.Context;
import android.database.Cursor;

import com.lib.bandaid.data.local.sqlite.core.builder.SqlBuilder;
import com.lib.bandaid.data.local.sqlite.pool.SessionFactory;
import com.lib.bandaid.data.local.sqlite.pool.SqLiteConnection;
import com.lib.bandaid.data.local.sqlite.proxy.mvc.base.Status;
import com.lib.bandaid.data.local.sqlite.proxy.mvc.dao.i.IMvcDao;
import com.lib.bandaid.data.local.sqlite.utils.CursorUtil;
import com.lib.bandaid.data.local.sqlite.utils.ReflectUtil;
import com.lib.bandaid.data.local.sqlite.utils.TransferUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zy on 2018/1/2.
 */

public class MvcDao<T> extends Status<T> implements IMvcDao<T> {


    public MvcDao(Context context, String dbPath, int version) {
        super(context, dbPath, version);
    }

    @Override
    public List<Map<String, Object>> queryTableStructures(String... tableNames) {
        List<Map<String, Object>> list = null;
        Cursor cursor = null;
        try {
            if (tableNames == null || tableNames.length == 0) return null;
            StringBuilder stringBuilder = new StringBuilder("(");
            for (int i = 0; i < tableNames.length; i++) {
                stringBuilder.append("'").append(tableNames[i]).append("'");
                if (i != tableNames.length - 1) {
                    stringBuilder.append(",");
                } else {
                    stringBuilder.append(")");
                }
            }
            String sql = "select * from sqlite_master where type = 'table' and name in " + stringBuilder.toString();
            cursor = query(sql);
            list = CursorUtil.cursor2ListMap(cursor);
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        } finally {
            SessionFactory.close(null, cursor);
        }
        return list;
    }

    @Override
    public Boolean createIfNecessary(Class t) {
        try {
            String createSql = SqlBuilder.getTableBuildingSQL(t.newInstance());
            return exec(createSql);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public IMvcDao<T> useSafe(Class t) {
        createIfNecessary(t);
        return this;
    }

    @Override
    public Boolean exeSql(String sql) {
        return exec(sql);
    }

    @Override
    public Boolean save(T t) {
        String sql = SqlBuilder.getSaveSQL(t);
        return exec(sql);
    }

    @Override
    public Boolean save(List<T> list) {
        List<String> sqls = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String sql = SqlBuilder.getSaveSQL(list.get(i));
            sqls.add(sql);
        }
        return exec(sqls);
    }

    @Override
    public Boolean update(T t) {
        String sql = SqlBuilder.getUpDateSQL(t);
        return exec(sql);
    }

    @Override
    public Boolean saveOrUpdate(T t) {
        boolean flag = false;
        if (isExisted(t.getClass(), ReflectUtil.getPrimaryMap(t))) {
            flag = update(t, true);
        } else {
            flag = save(t);
        }
        return flag;
    }

    @Override
    public Boolean update(T t, Boolean isUpDataNullValue) {
        String sql = SqlBuilder.getUpDateSQL(t, isUpDataNullValue);
        return exec(sql);
    }

    @Override
    public Boolean update(T t, String[] keyWords, Boolean isUpDataNullValue) {
        String sql = SqlBuilder.getUpDateSQL(t, keyWords, isUpDataNullValue);
        return exec(sql);
    }

    @Override
    public Boolean batchSaveUpDate(List<T> list) {
        onReady();
        boolean flag = false;
        SqLiteConnection conn = null;
        try {
            //获得数据库连接
            conn = SessionFactory.getCurrentSession(context, dbPath, version);
            conn.beginTransaction();//开启事物
            flag = true;
            String sql;
            //再保存
            for (int i = 0; i < list.size(); i++) {
                Object t = list.get(i);
                try {
                    sql = SqlBuilder.getSaveSQL(t);
                    conn.execSQL(sql);
                } catch (Exception e) {
                    String err = e.getMessage();
                    if (err.contains("UNIQUE constraint failed")) {
                        System.out.println("主键重复，采取更新");
                        try {
                            sql = SqlBuilder.getUpDateSQL(t, true);
                            conn.execSQL(sql);
                        } catch (Exception e1) {
                            flag = false;
                            System.out.println("更新失败");
                        }
                    } else {
                        flag = false;
                        System.out.println("保存失败");
                    }
                }
            }
            if (flag) {
                conn.setTransactionSuccessful();
            }
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
            ;
        } finally {
            conn.endTransaction();
            SessionFactory.close(conn, null);
        }
        return flag;
    }

    @Override
    public Boolean batchSaveUpDateV1(List<T> list) {
        onReady();
        boolean flag = false;
        SqLiteConnection conn = null;
        try {
            //获得数据库连接
            conn = SessionFactory.getCurrentSession(context, dbPath, version);
            conn.beginTransaction();//开启事物
            //先删除
            Class clazz = list.get(0).getClass();
            List<Map<String, Object>> listMap = ReflectUtil.getPrimaryList(list);
            for (int i = 0; i < listMap.size(); i++) {
                String sql = SqlBuilder.getDelSQLByMultiCondition(clazz, listMap.get(i));
                conn.execSQL(sql);
            }
            //再保存
            for (int i = 0; i < list.size(); i++) {
                conn.execSQL(SqlBuilder.getSaveSQL(list.get(i)));
            }
            conn.setTransactionSuccessful();
            flag = true;
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        } finally {
            conn.endTransaction();
            SessionFactory.close(conn, null);
        }
        return flag;
    }


    @Override
    public Boolean batchDelete(List<String> sqls) {
        return exec(sqls);
    }

    @Override
    public Boolean batchDelete(Class<?> clazz, List<Map<String, Object>> list) {
        List<String> sqls = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String sql = SqlBuilder.getDelSQLByMultiCondition(clazz, list.get(i));
            sqls.add(sql);
        }
        return exec(sqls);
    }

    @Override
    public Boolean delete(T t) {
        String sql = SqlBuilder.getDelSQLByMultiCondition(t.getClass(), ReflectUtil.getPrimaryMap(t));
        return exec(sql);
    }

    @Override
    public Boolean saveOrUpdate(T t, boolean isUpDataNullValue) {
        boolean flag = false;
        if (isExisted(t.getClass(), ReflectUtil.getPrimaryMap(t))) {
            flag = update(t, isUpDataNullValue);
        } else {
            flag = save(t);
        }
        return flag;
    }

    @Override
    public Boolean saveOrUpdate(T t, String[] keyWords, boolean isUpDataNullValue) {
        boolean flag = false;
        if (isExisted(t.getClass(), TransferUtil.T2Map(t, keyWords))) {
            flag = update(t, keyWords, isUpDataNullValue);
        } else {
            flag = save(t);
        }
        return flag;
    }

    @Override
    public Boolean delete(List<T> list) {
        List<String> sqls = new ArrayList<>();
        Class clazz = list.get(0).getClass();
        List<Map<String, Object>> listMap = ReflectUtil.getPrimaryList(list);
        for (int i = 0; i < listMap.size(); i++) {
            String sql = SqlBuilder.getDelSQLByMultiCondition(clazz, listMap.get(i));
            sqls.add(sql);
        }
        return exec(sqls);
    }


    @Override
    public Boolean delete(Class<?> clazz, Map<String, Object> map) {
        String sql = SqlBuilder.getDelSQLByMultiCondition(clazz, map);
        return exec(sql);
    }

    @Override
    public Boolean delete(Class<?> clazz, String where) {
        String sql = SqlBuilder.getDelSQLByWhere(clazz, where);
        boolean flag = false;
        try {
            flag = exec(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public Boolean isExisted(Class<?> clazz, Map<String, Object> map) {
        String sql = SqlBuilder.getRecordSQLByMultiCondition(clazz, map);
        return exist(sql);
    }

    @Override
    public Boolean isExisted(Class<?> clazz, String where) {
        String sql = SqlBuilder.getRecordsCountSQL(clazz, where);
        return exist(sql);
    }


    @Override
    public Map<String, Object> getMapBySql(String sql) {
        Map<String, Object> map = null;
        Cursor cursor = null;
        try {
            cursor = query(sql);
            map = CursorUtil.cursor2Map(cursor);
        } catch (Exception e) {
            map = null;
            e.printStackTrace();
            ;
        } finally {
            SessionFactory.close(null, cursor);
        }
        return map;
    }

    @Override
    public List<T> getListTByMultiCondition(Class<?> clazz, Map<String, Object> map) {
        List<T> list = null;
        Cursor cursor = null;
        try {
            String sql = SqlBuilder.getRecordSQLByMultiCondition(clazz, map);
            cursor = query(sql);
            list = (List<T>) CursorUtil.cursor2ListV1(clazz, cursor);
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
            ;
        } finally {
            SessionFactory.close(null, cursor);
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> getListMapBySql(String sql) {
        List<Map<String, Object>> list = null;
        Cursor cursor = null;
        try {
            cursor = query(sql);
            list = CursorUtil.cursor2ListMap(cursor);
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
            ;
        } finally {
            SessionFactory.close(null, cursor);
        }
        return list;
    }

    @Override
    public T getTByMultiCondition(Class<?> clazz, Map<String, Object> map) {
        T t = null;
        Cursor cursor = null;
        try {
            String sql = SqlBuilder.getRecordSQLByMultiCondition(clazz, map);
            cursor = query(sql);
            t = (T) CursorUtil.cursor2EntityV1(clazz, cursor);
        } catch (Exception e) {
            t = null;
            e.printStackTrace();
            ;
        } finally {
            SessionFactory.close(null, cursor);
        }
        return t;
    }

    @Override
    public List<T> getListTByWhere(Class<?> clazz, String where) {
        List<T> list = null;
        Cursor cursor = null;
        try {
            String sql = SqlBuilder.getRecordSQLByWhere(clazz, where);
            cursor = query(sql);
            list = (List<T>) CursorUtil.cursor2ListV1(clazz, cursor);
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
            ;
        } finally {
            SessionFactory.close(null, cursor);
        }
        return list;
    }

    @Override
    public List<T> getListTBySql(Class<?> clazz, String sql) {
        List<T> list = null;
        Cursor cursor = null;
        try {
            cursor = query(sql);
            list = (List<T>) CursorUtil.cursor2ListV1(clazz, cursor);
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
            ;
        } finally {
            SessionFactory.close(null, cursor);
        }
        return list;
    }

    @Override
    public List<T> getListTBySql(Class<?> clazz, String sql, boolean isDbColumn) {
        List<T> list = null;
        Cursor cursor = null;
        try {
            cursor = query(sql);
            list = (List<T>) CursorUtil.cursor2ListV3(clazz, cursor, isDbColumn);
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
            ;
        } finally {
            SessionFactory.close(null, cursor);
        }
        return list;
    }

    @Override
    public T getTBySql(Class<?> clazz, String sql, boolean isDbColumn) {
        T t = null;
        Cursor cursor = null;
        try {
            cursor = query(sql);
            t = (T) CursorUtil.cursor2EntityV2(clazz, cursor, isDbColumn);
        } catch (Exception e) {
            t = null;
            e.printStackTrace();
            ;
        } finally {
            SessionFactory.close(null, cursor);
        }
        return t;
    }

    @Override
    public T getTBySql(Class<?> clazz, String sql) {
        return getTBySql(clazz, sql, false);
    }
}
