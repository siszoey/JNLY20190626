package com.lib.bandaid.data.local.sqlite.proxy.mvc.dao.i;

import java.util.List;
import java.util.Map;

/**
 * Created by zy on 2018/1/2.
 */

public interface IMvcDao<T> {

    /**
     *
     * @param tableNames
     * @return
     */
    public List<Map<String, Object>> queryTableStructures(String... tableNames);

    /**
     *
     * @param t
     * @return
     */
    public Boolean createIfNecessary(Class t);

    /**
     *
     * @param t
     * @return
     */
    public IMvcDao<T> useSafe(Class t);

    /**
     *
     * @param sql
     * @return
     */
    public Boolean exeSql(String sql);

    /**
     * 保存
     *
     * @param t
     * @return
     */
    public Boolean save(T t);

    /**
     * 批量编辑
     *
     * @param list
     * @return
     */
    public Boolean save(List<T> list);

    /**
     * 更新历史记录
     *
     * @param t
     * @return
     */
    public Boolean update(T t);

    /**
     * @param t
     * @return
     */
    public Boolean saveOrUpdate(T t);

    /**
     * @param t
     * @param isUpDataNullValue
     * @return
     */
    public Boolean update(T t, Boolean isUpDataNullValue);

    /**
     * @param t
     * @param keyWords
     * @param isUpDataNullValue
     * @return
     */
    public Boolean update(T t, String[] keyWords, Boolean isUpDataNullValue);

    /**
     * @param list
     * @return
     */
    public Boolean batchSaveUpDate(List<T> list);

    /**
     * @param list
     * @return
     */
    public Boolean batchSaveUpDateV1(List<T> list);

    /**
     * @param sqls
     * @return
     */
    public Boolean batchDelete(List<String> sqls);

    /**
     * @param clazz
     * @param list
     * @return
     */
    public Boolean batchDelete(Class<?> clazz, List<Map<String, Object>> list);

    /**
     * 删除历史记录
     *
     * @param t
     * @return
     */
    public Boolean delete(T t);

    /**
     * @param t
     * @param isUpDataNullValue
     * @return
     */
    public Boolean saveOrUpdate(T t, boolean isUpDataNullValue);

    /**
     * @param t
     * @param keyWords
     * @param isUpDataNullValue
     * @return
     */
    public Boolean saveOrUpdate(T t, String[] keyWords, boolean isUpDataNullValue);

    /**
     * @param list
     * @return
     */
    public Boolean delete(List<T> list);

    /**
     * 指定符合条件删除记录
     *
     * @param clazz
     * @param map
     * @return
     */
    public Boolean delete(Class<?> clazz, Map<String, Object> map);

    /**
     * @param clazz
     * @param where
     * @return
     */
    public Boolean delete(Class<?> clazz, String where);

    /**
     * @param clazz
     * @param map
     * @return
     */
    public Boolean isExisted(Class<?> clazz, Map<String, Object> map);

    /**
     * @param clazz
     * @param where
     * @return
     */
    public Boolean isExisted(Class<?> clazz, String where);

    /**
     * @param sql
     * @return
     */
    public Map<String, Object> getMapBySql(String sql);

    /**
     * @param clazz
     * @param map
     * @return
     */
    public List<T> getListTByMultiCondition(Class<?> clazz, Map<String, Object> map);

    /**
     * @param sql
     * @return
     */
    public List<Map<String, Object>> getListMapBySql(String sql);

    /**
     * 单条记录复合条件查询
     *
     * @param clazz
     * @param map
     * @return
     */
    public T getTByMultiCondition(Class<?> clazz, Map<String, Object> map);

    /**
     * @param clazz
     * @param where
     * @return
     */
    public List<T> getListTByWhere(Class<?> clazz, String where);

    /**
     * @param clazz
     * @param sql
     * @return
     */
    public List<T> getListTBySql(Class<?> clazz, String sql);

    /**
     * @param clazz
     * @param sql
     * @param _2DbColumn
     * @return
     */
    public List<T> getListTBySql(Class<?> clazz, String sql, boolean _2DbColumn);

    /**
     * @param clazz
     * @param sql
     * @param _2DbColumn
     * @return
     */
    public T getTBySql(Class<?> clazz, String sql, boolean _2DbColumn);

    public T getTBySql(Class<?> clazz, String sql);
}
