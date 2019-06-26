package com.lib.bandaid.data.local.sqlite.proxy.mvc.service.s;

import android.content.Context;

import com.lib.bandaid.data.local.sqlite.proxy.mvc.dao.d.MvcDao;
import com.lib.bandaid.data.local.sqlite.proxy.mvc.dao.i.IMvcDao;
import com.lib.bandaid.data.local.sqlite.proxy.mvc.service.i.IMvcService;

import java.util.List;
import java.util.Map;

/**
 * Created by zy on 2018/1/2.
 */

public class MvcService<T> implements IMvcService<T> {

    private IMvcDao<T> iMvcDao;

    public MvcService(Context context, String dbPath, int version) {
        this.iMvcDao = new MvcDao<T>(context, dbPath, version);
    }

    @Override
    public List<Map<String, Object>> queryTableStructures(String... tableNames) {
        return iMvcDao.queryTableStructures(tableNames);
    }

    @Override
    public Boolean createIfNecessary(Class t) {
        return iMvcDao.createIfNecessary(t);
    }

    @Override
    public IMvcDao<T> useSafe(Class t) {
        iMvcDao.useSafe(t);
        return this;
    }

    @Override
    public Boolean exeSql(String sql) {
        return iMvcDao.exeSql(sql);
    }

    @Override
    public Boolean save(T t) {
        return iMvcDao.save(t);
    }

    @Override
    public Boolean save(List<T> list) {
        return iMvcDao.save(list);
    }

    @Override
    public Boolean update(T t) {
        return iMvcDao.update(t);
    }

    @Override
    public Boolean saveOrUpdate(T t) {
        return iMvcDao.saveOrUpdate(t);
    }

    @Override
    public Boolean update(T t, Boolean isUpDataNullValue) {
        return iMvcDao.update(t, isUpDataNullValue);
    }

    @Override
    public Boolean update(T t, String[] keyWords, Boolean isUpDataNullValue) {
        return iMvcDao.update(t, keyWords, isUpDataNullValue);
    }

    @Override
    public Boolean batchSaveUpDate(List<T> list) {
        return iMvcDao.batchSaveUpDate(list);
    }

    @Override
    public Boolean batchSaveUpDateV1(List<T> list) {
        return iMvcDao.batchSaveUpDateV1(list);
    }

    @Override
    public Boolean batchDelete(List<String> sqls) {
        return iMvcDao.batchDelete(sqls);
    }

    @Override
    public Boolean batchDelete(Class<?> clazz, List<Map<String, Object>> list) {
        return iMvcDao.batchDelete(clazz, list);
    }

    @Override
    public Boolean delete(T t) {
        return iMvcDao.delete(t);
    }

    @Override
    public Boolean saveOrUpdate(T t, boolean isUpDataNullValue) {
        return iMvcDao.saveOrUpdate(t, isUpDataNullValue);
    }

    @Override
    public Boolean saveOrUpdate(T t, String[] keyWords, boolean isUpDataNullValue) {
        return iMvcDao.saveOrUpdate(t, keyWords, isUpDataNullValue);
    }


    @Override
    public Boolean delete(List<T> list) {
        return iMvcDao.delete(list);
    }


    @Override
    public Boolean delete(Class<?> clazz, Map<String, Object> map) {
        return iMvcDao.delete(clazz, map);
    }

    @Override
    public Boolean delete(Class<?> clazz, String where) {
        return iMvcDao.delete(clazz, where);
    }


    @Override
    public Boolean isExisted(Class<?> clazz, Map<String, Object> map) {
        return iMvcDao.isExisted(clazz, map);
    }

    @Override
    public Boolean isExisted(Class<?> clazz, String where) {
        return iMvcDao.isExisted(clazz, where);
    }


    @Override
    public Map<String, Object> getMapBySql(String sql) {
        return iMvcDao.getMapBySql(sql);
    }

    @Override
    public List<T> getListTByMultiCondition(Class<?> clazz, Map<String, Object> map) {
        return iMvcDao.getListTByMultiCondition(clazz, map);
    }

    @Override
    public List<Map<String, Object>> getListMapBySql(String sql) {
        return iMvcDao.getListMapBySql(sql);
    }

    @Override
    public T getTByMultiCondition(Class<?> clazz, Map<String, Object> map) {
        return iMvcDao.getTByMultiCondition(clazz, map);
    }

    @Override
    public List<T> getListTByWhere(Class<?> clazz, String where) {
        return iMvcDao.getListTByWhere(clazz, where);
    }

    @Override
    public List<T> getListTBySql(Class<?> clazz, String sql) {
        return iMvcDao.getListTBySql(clazz, sql);
    }

    @Override
    public List<T> getListTBySql(Class<?> clazz, String sql, boolean isDbColumn) {
        return iMvcDao.getListTBySql(clazz, sql, isDbColumn);
    }

    @Override
    public T getTBySql(Class<?> clazz, String sql, boolean isDbColumn) {
        return iMvcDao.getTBySql(clazz, sql, isDbColumn);
    }

    @Override
    public T getTBySql(Class<?> clazz, String sql) {
        return iMvcDao.getTBySql(clazz, sql);
    }
}
