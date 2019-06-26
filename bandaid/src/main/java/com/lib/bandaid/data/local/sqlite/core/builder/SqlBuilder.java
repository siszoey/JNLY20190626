package com.lib.bandaid.data.local.sqlite.core.builder;

import com.lib.bandaid.data.local.sqlite.core.annotation.Column;
import com.lib.bandaid.data.local.sqlite.core.annotation.Format;
import com.lib.bandaid.data.local.sqlite.core.annotation.Table;
import com.lib.bandaid.data.local.sqlite.utils.DateUtil;
import com.lib.bandaid.data.local.sqlite.utils.ReflectUtil;
import com.lib.bandaid.data.local.sqlite.utils.TransferUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by zy on 2017/1/13.
 */

public final class SqlBuilder {

    static final Map<Class<?>, String> TYPES;

    static {
        TYPES = new HashMap<Class<?>, String>();
        TYPES.put(Byte.class, "BYTE");
        TYPES.put(Boolean.class, "INTEGER");//数据库没有boolen类型
        TYPES.put(Short.class, "SHORT");
        TYPES.put(Integer.class, "INTEGER");
        TYPES.put(Long.class, "LONG");
        TYPES.put(String.class, "TEXT");
        TYPES.put(Byte[].class, "BLOB");
        TYPES.put(Float.class, "FLOAT"); // REAL
        TYPES.put(Double.class, "DOUBLE"); // REAL
        TYPES.put(Date.class, "DATETIME");
    }


    /**
     * 生成表
     *
     * @param obj
     * @return
     */
    public static String getTableBuildingSQL(Object obj) {
        String sql = "";
        String className = "";
        boolean hasPk = false;
        try {
            Class<?> clazz;
            if (obj instanceof Class) {
                clazz = (Class<?>) obj;
            } else {
                clazz = obj.getClass();
            }
            String[] columnArray = ReflectUtil.columnArray(clazz);
            StringBuilder strBuilder = new StringBuilder("create table if not exists ");
            StringBuilder pkBuilder = new StringBuilder("primary key (");
            String tableName = ReflectUtil.getTableName(clazz);
            strBuilder.append(tableName);
            strBuilder.append("(");
            List<Field> fields = ReflectUtil.getIterationFields(clazz);
            String name;
            Field f;
            Column column;
            boolean columnPKey;
            String columnType;
            String columnName;
            boolean autoincrement;
            String type;
            for (int k = 0; columnArray != null && k < columnArray.length; k++) {
                name = columnArray[k];
                for (int i = 0; i < fields.size(); i++) {
                    f = fields.get(i);
                    if (f.getName().equals(name)) {
                        type = TYPES.get(f.getType());
                        if (type == null) {
                            continue;
                        } else {
                            if (f.isAnnotationPresent(Column.class)) {
                                column = f.getAnnotation(Column.class);
                                columnPKey = column.isPKey();//主键
                                columnType = column.type();//数据类型
                                autoincrement = column.autoincrement();
                                if (columnType.equals("default")) {
                                    columnType = type;
                                }
                                columnName = ReflectUtil.getColumnName(f);
                                if (columnPKey) {
                                    hasPk = true;
                                    //if (columnType.trim().toLowerCase().contains("autoincrement")) {
                                    if (autoincrement) {
                                        strBuilder.append(columnName + " " + columnType).append(",");
                                        pkBuilder.append(columnName).append(" autoincrement ").append(",");
                                    } else {
                                        strBuilder.append(columnName + " " + columnType).append(",");
                                        pkBuilder.append(columnName).append(",");
                                    }
                                } else {
                                    strBuilder.append(columnName + " " + columnType).append(",");
                                }
                            }
                        }
                        break;
                    }
                }
            }
            if (!strBuilder.toString().endsWith(",")) return null;
            if (hasPk) {
                int endPk = pkBuilder.lastIndexOf(",");
                pkBuilder = pkBuilder.deleteCharAt(endPk);
                pkBuilder.append(" )");
                if (pkBuilder.indexOf("autoincrement") > 0 && pkBuilder.indexOf(",") > 0) {
                    System.err.println("sqlite 不支持自增字段的联合主键，请返回实体修改 !☹!");
                }
                sql = strBuilder.append(pkBuilder) + ")";
            } else {
                int end = strBuilder.lastIndexOf(",");
                sql = strBuilder.substring(0, end) + ")";
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (e.toString().contains("StringIndexOutOfBoundsException")) {
                System.out.println("请添加" + className + ".class，默认的toString方法");
            }
        }
        return sql;
    }


    public static String getSaveSQL(Object obj) {
        StringBuilder sqlBuilder = new StringBuilder("insert into ");
        Class<? extends Object> clazz = obj.getClass();
        String tableName = ReflectUtil.getTableName(clazz);
        Map KV = TransferUtil.getTKVSql(obj);
        String key = KV.get("Key").toString();
        String val = KV.get("Value").toString();
        sqlBuilder.append(tableName).append("(").append(key).append(") VALUES (").append(val).append(")");
        return sqlBuilder.toString();
    }


    /**
     * 更新一条数据的sql(无法只更新id)
     *
     * @param obj
     * @return
     */

    public static String getUpDateSQL(Object obj) {
        StringBuilder sqlBuilder = null;
        StringBuilder values = null;
        StringBuilder where = null;
        try {
            sqlBuilder = new StringBuilder("update ");
            values = new StringBuilder(" set ");
            where = new StringBuilder(" where ");
            Class<? extends Object> clazz = obj.getClass();
            String tableName = ReflectUtil.getTableName(obj.getClass());
            sqlBuilder.append(tableName);
            List<Field> fields = ReflectUtil.getIterationFields(clazz);
            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                if (field.isAnnotationPresent(Column.class)) {
                    Column column = field.getAnnotation(Column.class);
                    Boolean columnPKey = column.isPKey();//主键
                    String columnName = ReflectUtil.getColumnName(field);
                    String getMethodName = ReflectUtil.getMethodName(field);
                    Method getMethod = clazz.getMethod(getMethodName, new Class[]{});
                    Object value = getMethod.invoke(obj, new Object[]{});//获取属性值
                    if (value == null) continue;
                    Class<?> type = field.getType();//获取属性类型
                    if (type == Date.class) { //属性按格式转换
                        if (getMethod.isAnnotationPresent(Format.class)) { //如果指定了 日期的转换格式则执行转换
                            Format format = getMethod.getAnnotation(Format.class);
                            String pattern = format.dateTimePattern();
                            value = DateUtil.getDAT(pattern, (Date) value);
                        } else {
                            value = DateUtil.getDAT("yyyy-MM-dd HH:mm:ss", (Date) value);
                        }
                    }
                    if (!columnPKey) {
                        String s = TransferUtil.objectToSqlKeyValStr(type, columnName, value);
                        values.append(s);
                        values.append(" ,");
                    } else {
                        String s = TransferUtil.objectToSqlKeyValStr(type, columnName, value);
                        where.append(s);
                        where.append(" and ");
                    }
                }
            }
            if (!values.toString().endsWith(",")) return null;
            int endValues = values.lastIndexOf(",");
            values.delete(endValues, endValues + 1);

            if (!where.toString().endsWith(" and ")) return null;
            endValues = where.lastIndexOf(" and ");
            where.delete(endValues, endValues + 5);
            sqlBuilder.append(values).append(where);
        } catch (Exception e) {
            e.printStackTrace();
            sqlBuilder = null;
        }
        return sqlBuilder == null ? null : sqlBuilder.toString();
    }


    /**
     * 更新一条数据的sql(无法只更新id)
     *
     * @param obj
     * @return
     */
    public static String getUpDateSQL(Object obj, boolean isUpDataNullValue) {
        StringBuilder sqlBuilder = null;
        StringBuilder values = null;
        StringBuilder where = null;
        try {
            sqlBuilder = new StringBuilder("update ");
            values = new StringBuilder(" set ");
            where = new StringBuilder(" where ");
            Class<? extends Object> clazz = obj.getClass();
            String tableName = ReflectUtil.getTableName(obj.getClass());
            sqlBuilder.append(tableName);
            List<Field> fields = ReflectUtil.getIterationFields(clazz);
            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                if (field.isAnnotationPresent(Column.class)) {
                    Column column = field.getAnnotation(Column.class);
                    Boolean columnPKey = column.isPKey();//主键
                    String columnName = ReflectUtil.getColumnName(field);
                    String getMethodName = ReflectUtil.getMethodName(field);
                    Method getMethod = clazz.getMethod(getMethodName, new Class[]{});
                    Object value = getMethod.invoke(obj, new Object[]{});//获取属性值
                    if (!isUpDataNullValue) {
                        if (value == null) continue;
                    }

                    Class<?> type = field.getType();//获取属性类型
                    if (type == Date.class) { //属性按格式转换
                        if (getMethod.isAnnotationPresent(Format.class)) { //如果指定了 日期的转换格式则执行转换
                            Format format = getMethod.getAnnotation(Format.class);
                            String pattern = format.dateTimePattern();
                            value = DateUtil.getDAT(pattern, (Date) value);
                        } else {
                            value = DateUtil.getDAT("yyyy-MM-dd HH:mm:ss", (Date) value);
                        }
                    }
                    if (!columnPKey) {
                        String s = TransferUtil.objectToSqlKeyValStr(type, columnName, value);
                        values.append(s);
                        values.append(" ,");
                    } else {
                        String s = TransferUtil.objectToSqlKeyValStr(type, columnName, value);
                        where.append(s);
                        where.append(" and ");
                    }
                }
            }
            if (!values.toString().endsWith(",")) return null;
            int endValues = values.lastIndexOf(",");
            values.delete(endValues, endValues + 1);

            if (!where.toString().endsWith(" and ")) return null;
            endValues = where.lastIndexOf(" and ");
            where.delete(endValues, endValues + 5);
            sqlBuilder.append(values).append(where);
        } catch (Exception e) {
            e.printStackTrace();
            sqlBuilder = null;
        }
        return sqlBuilder == null ? null : sqlBuilder.toString();
    }


    /**
     * 更新一条数据的sql(指定where的关键字)
     *
     * @param obj               //实体
     * @param keyWords          //指定的where
     * @param isUpDataNullValue //遇到null是否更新
     * @return
     */
    public static String getUpDateSQL(Object obj, String[] keyWords, boolean isUpDataNullValue) {
        StringBuilder sqlBuilder = null;
        StringBuilder values = null;
        StringBuilder where = null;
        try {
            sqlBuilder = new StringBuilder("update ");
            values = new StringBuilder(" set ");
            where = new StringBuilder(" where ");
            Class<? extends Object> clazz = obj.getClass();
            String tableName = ReflectUtil.getTableName(obj.getClass());
            sqlBuilder.append(tableName);
            List<Field> fields = ReflectUtil.getIterationFields(clazz);
            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                if (field.isAnnotationPresent(Column.class)) {
                    String columnName = ReflectUtil.getColumnName(field);
                    Boolean isWhere = false;
                    for (int j = 0; j < keyWords.length; j++) {
                        if (columnName.equals(keyWords[j])) {
                            isWhere = true;
                            break;
                        }
                    }
                    String getMethodName = ReflectUtil.getMethodName(field);
                    Method getMethod = clazz.getMethod(getMethodName, new Class[]{});
                    Object value = getMethod.invoke(obj, new Object[]{});//获取属性值
                    Class<?> type = field.getType();//获取属性类型
                    if (value == null) {
                        if (!isUpDataNullValue) {
                            continue;
                        } else {
                            type = String.class;
                            value = "null";
                        }
                    }
                    if (type == Date.class) { //属性按格式转换
                        if (getMethod.isAnnotationPresent(Format.class)) { //如果指定了 日期的转换格式则执行转换
                            Format format = getMethod.getAnnotation(Format.class);
                            String pattern = format.dateTimePattern();
                            value = DateUtil.getDAT(pattern, (Date) value);
                        } else {
                            value = DateUtil.getDAT("yyyy-MM-dd HH:mm:ss", (Date) value);
                        }
                    }
                    if (!isWhere) {
                        values.append(TransferUtil.objectToSqlKeyValStr(type, columnName, value));
                        values.append(" ,");
                    } else { //where 更新条件
                        where.append(TransferUtil.objectToSqlKeyValStr(type, columnName, value));
                        where.append(" and ");
                    }
                }
            }
            if (!values.toString().endsWith(",")) return null;
            int endValues = values.lastIndexOf(",");
            values.delete(endValues, endValues + 1);
            where.delete(where.length() - 5, where.length());
            sqlBuilder.append(values).append(where);
        } catch (Exception e) {
            e.printStackTrace();
            sqlBuilder = null;
        }
        return sqlBuilder == null ? null : sqlBuilder.toString();
    }


    /**
     * 通过一个主键，获取一条数据的sql
     *
     * @param clazz
     * @return
     */
    public static String getRecordSQL(Class<?> clazz, Object id) {
        StringBuilder sqlBuilder = null;
        StringBuilder values = null;
        StringBuilder where = null;
        try {
            sqlBuilder = new StringBuilder("select ");
            values = new StringBuilder();
            where = new StringBuilder(" where ");
            Table table = clazz.getAnnotation(Table.class);
            String tableName = table.name();
            if (tableName.equals("default")) {
                tableName = clazz.getSimpleName();
            }
            List<Field> fields = ReflectUtil.getIterationFields(clazz);
            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                if (field.isAnnotationPresent(Column.class)) {
                    Column column = field.getAnnotation(Column.class);
                    Boolean columnPKey = column.isPKey();//主键
                    String columnName = column.name();//列名(自定义的列名称)
                    if (columnName.equals("default")) {
                        columnName = field.getName();//默认的列名称
                    }//获取列名
                    Class<?> type = field.getType();//获取属性类型
                    values.append(columnName);
                    values.append(" as ");
                    values.append(field.getName());//转换成实体属性字段
                    values.append(" ,");
                    if (columnPKey) {
                        if (type == Integer.class) {
                            where.append(columnName);
                            where.append(" = ");
                            where.append(id);
                        } else if (type == Double.class) {
                            where.append(columnName);
                            where.append(" = ");
                            where.append(id);
                        } else if (type == Float.class) {
                            where.append(columnName);
                            where.append(" = ");
                            where.append(id);
                        } else if (type == Long.class) {
                            where.append(columnName);
                            where.append(" = ");
                            where.append(id);
                        } else if (type == Short.class) {
                            where.append(columnName);
                            where.append(" = ");
                            where.append(id);
                        } else if (type == String.class) {
                            where.append(columnName);
                            where.append(" = '");
                            where.append(id);
                            where.append("' ");
                        } else if (type == Date.class) {
                            where.append(columnName);
                            where.append(" = '");
                            where.append(id);
                            where.append("' ");
                        } else {
                            where.append(columnName);
                            where.append(" = '");
                            where.append(id);
                            where.append("'");
                        }
                        where.append(" and ");
                    }
                }
            }
            if (!values.toString().endsWith(",")) return null;
            if (!where.toString().endsWith(" and ")) return null;
            int endValues = values.lastIndexOf(",");
            values.delete(endValues, endValues + 1);
            where.delete(where.length() - 5, where.length());
            sqlBuilder.append(values);
            sqlBuilder.append(" from ");
            sqlBuilder.append(tableName);
            sqlBuilder.append(where);
        } catch (Exception e) {
            e.printStackTrace();
            sqlBuilder = null;
        }
        return sqlBuilder == null ? null : sqlBuilder.toString();
    }


    /**
     * 通过主键集合，获取多条数据的sql
     *
     * @param clazz
     * @return
     */
    public static String getRecordsSQL(Class<?> clazz, List<Object> ids) {
        StringBuilder sqlBuilder = null;
        StringBuilder values = null;
        StringBuilder where = null;
        try {
            sqlBuilder = new StringBuilder("select ");
            values = new StringBuilder();
            where = new StringBuilder(" where ");
            if (ids == null || ids.size() == 0) {
                return null;
            }
            String tableName = ReflectUtil.getTableName(clazz);
            List<Field> fields = ReflectUtil.getIterationFields(clazz);
            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                if (field.isAnnotationPresent(Column.class)) {
                    Boolean columnPKey = ReflectUtil.isPKey(field);//主键
                    String columnName = ReflectUtil.getColumnName(field);
                    Class<?> type = field.getType();//获取属性类型
                    values.append(columnName);
                    values.append(" as ");
                    values.append(field.getName());//转换成实体属性字段
                    values.append(" ,");
                    if (columnPKey) {
                        where.append(columnName);
                        where.append(" in (");
                        for (int j = 0; j < ids.size(); j++) {
                            if (type == Integer.class) {
                                where.append(ids.get(j));
                                where.append(",");
                            } else if (type == Double.class) {
                                where.append(ids.get(j));
                                where.append(",");
                            } else if (type == Float.class) {
                                where.append(ids.get(j));
                                where.append(",");
                            } else if (type == Long.class) {
                                where.append(ids.get(j));
                                where.append(",");
                            } else if (type == Short.class) {
                                where.append(ids.get(j));
                                where.append(",");
                            } else if (type == String.class) {
                                where.append("'");
                                where.append(ids.get(j));
                                where.append("',");
                            } else if (type == Date.class) {
                                where.append("'");
                                where.append(ids.get(j));
                                where.append("',");
                            } else {
                                where.append("'");
                                where.append(ids.get(j));
                                where.append("',");
                            }
                        }
                        //break;
                    } else {
                        continue;
                    }
                }
            }
            if (!values.toString().endsWith(",")) return null;
            int endValues = values.lastIndexOf(",");
            values.delete(endValues, endValues + 1);
            sqlBuilder.append(values);
            sqlBuilder.append(" from ");
            sqlBuilder.append(tableName);
            if (!where.toString().endsWith(",")) return null;
            int endWhere = where.lastIndexOf(",");
            where.delete(endWhere, endWhere + 1);
            where.append(")");
            sqlBuilder.append(where);
        } catch (Exception e) {
            e.printStackTrace();
            sqlBuilder = null;
        }
        return sqlBuilder == null ? null : sqlBuilder.toString();
    }


    /**
     * 获取sql,(指定where的多关键字)
     *
     * @param clazz 实体
     * @param map   指定的筛选条件组合(简单的"=" 条件，其他条件不行！！！！)
     * @return
     */
    public static String getRecordSQLByMultiCondition(Class<?> clazz, Map<String, Object> map) {
        StringBuilder sqlBuilder = null;
        StringBuilder values = null;
        StringBuilder where = null;
        boolean isAll = false;
        try {
            sqlBuilder = new StringBuilder("select ");
            values = new StringBuilder(" from ");
            where = new StringBuilder(" where ");
            String tableName = ReflectUtil.getTableName(clazz);
            values.append(tableName);
            List<Field> fields = ReflectUtil.getIterationFields(clazz);
            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                if (field.isAnnotationPresent(Column.class)) {
                    String columnName = ReflectUtil.getColumnName(field);
                    String getMethodName = ReflectUtil.getMethodName(field);
                    Method getMethod = clazz.getMethod(getMethodName, new Class[]{});
                    Class<?> type = field.getType();//获取属性类型
                    sqlBuilder.append(columnName);
                    sqlBuilder.append(" as ");
                    sqlBuilder.append(field.getName());//转换成实体属性字段
                    sqlBuilder.append(" ,");
                    if (map != null) {
                        isAll = false;
                        for (String key : map.keySet()) {
                            String w = key;
                            Object v = map.get(w);
                            if (columnName.equals(w)) {
                                if (type == Date.class) { //属性按格式转换
                                    if (getMethod.isAnnotationPresent(Format.class)) { //如果指定了 日期的转换格式则执行转换
                                        Format format = getMethod.getAnnotation(Format.class);
                                        String pattern = format.dateTimePattern();
                                        v = DateUtil.getDAT(pattern, (Date) v);
                                    } else {
                                        v = DateUtil.getDAT("yyyy-MM-dd HH:mm:ss", (Date) v);
                                    }
                                }
                                where.append(TransferUtil.objectToSqlKeyValStr(type, columnName, v));
                                where.append(" and ");
                            }
                        }

                    } else {
                        //System.out.println("查询所有");
                        isAll = true;
                    }
                }
            }
            if (!sqlBuilder.toString().endsWith(",")) return null;
            int endValues = sqlBuilder.lastIndexOf(",");
            sqlBuilder.delete(endValues, endValues + 1);
            if (!isAll) {
                where.delete(where.length() - 5, where.length());
                sqlBuilder.append(values).append(where);
            } else {
                sqlBuilder.append(values);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sqlBuilder = null;
        }
        return sqlBuilder == null ? null : sqlBuilder.toString();
    }

    public static String getRecordsCountSQL(Class<?> clazz, String where) {
        StringBuilder sqlBuilder = null;
        StringBuilder values = null;
        try {
            sqlBuilder = new StringBuilder("select count ( ");
            sqlBuilder.append(ReflectUtil.getPrimaryKeyName(clazz));
            sqlBuilder.append(" ) as count");
            values = new StringBuilder(" from ");
            String tableName = ReflectUtil.getTableName(clazz);
            values.append(tableName);
            if (where != null) {
                sqlBuilder.append(values).append(" " + where);
            } else {
                sqlBuilder.append(values);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sqlBuilder = null;
        }
        return sqlBuilder == null ? null : sqlBuilder.toString();
    }

    /**
     * 获取一条数据的sql,
     *
     * @param clazz 实体
     * @param where 由自己指定，where = null 时查询所有
     * @return
     */
    public static String getRecordSQLByWhere(Class<?> clazz, String where) {
        StringBuilder sqlBuilder = null;
        StringBuilder values = null;
        try {
            sqlBuilder = new StringBuilder("select ");
            values = new StringBuilder(" from ");
            String tableName = ReflectUtil.getTableName(clazz);
            values.append(tableName);
            //Field[] fields = clazz.getDeclaredFields();
            List<Field> fields = ReflectUtil.getIterationFields(clazz);
            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                if (field.isAnnotationPresent(Column.class)) {
                    String columnName = ReflectUtil.getColumnName(field);
                    sqlBuilder.append(columnName);
                    sqlBuilder.append(" as ");
                    sqlBuilder.append(field.getName());//转换成实体属性字段
                    sqlBuilder.append(" ,");
                }
            }
            if (!sqlBuilder.toString().endsWith(",")) return null;
            int endValues = sqlBuilder.lastIndexOf(",");
            sqlBuilder.delete(endValues, endValues + 1);
            if (where != null) {
                sqlBuilder.append(values).append(" " + where);
            } else {
                sqlBuilder.append(values);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sqlBuilder = null;
        }
        return sqlBuilder == null ? null : sqlBuilder.toString();
    }

    /**
     * 没写好！
     *
     * @param clazz
     * @param condition
     * @return
     */
    public static String getMultiPKDelSql(Class<?> clazz, Map<String, Object> condition) {
        StringBuilder sqlBuilder = new StringBuilder();
        StringBuilder sqlWhere = new StringBuilder();
        try {
            sqlBuilder.append("delete from ");
            sqlWhere.append(" where ");
            String tableName = ReflectUtil.getTableName(clazz);
            sqlBuilder.append(tableName);
            //Field[] fields = clazz.getDeclaredFields();
            List<Field> fields = ReflectUtil.getIterationFields(clazz);
            for (int i = 0; i < fields.size(); i++) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return null;
    }

    /**
     * 不支持复合主键
     * 通过主键，删除一条数据的sql
     *
     * @param clazz
     * @return
     */
    public static String getDelSQL(Class<?> clazz, Object id) {
        StringBuilder sqlBuilder = null;
        StringBuilder where = null;
        try {
            sqlBuilder = new StringBuilder("delete from ");
            where = new StringBuilder(" where ");
            Table table = clazz.getAnnotation(Table.class);
            String tableName = table.name();
            if (tableName.equals("default")) {
                tableName = clazz.getSimpleName();
            }
            sqlBuilder.append(tableName);
            List<Field> fields = ReflectUtil.getIterationFields(clazz);
            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                if (field.isAnnotationPresent(Column.class)) {
                    Column column = field.getAnnotation(Column.class);
                    Boolean columnPKey = column.isPKey();//主键
                    String columnName = column.name();//列名(自定义的列名称)
                    if (columnName.equals("default")) {
                        columnName = field.getName();//默认的列名称
                    }//获取列名
                    Class<?> type = field.getType();//获取属性类型
                    if (columnPKey) {
                        if (type == Integer.class) {
                            where.append(columnName);
                            where.append(" = ");
                            where.append(id);
                        } else if (type == Double.class) {
                            where.append(columnName);
                            where.append(" = ");
                            where.append(id);
                        } else if (type == Float.class) {
                            where.append(columnName);
                            where.append(" = ");
                            where.append(id);
                        } else if (type == Long.class) {
                            where.append(columnName);
                            where.append(" = ");
                            where.append(id);
                        } else if (type == Short.class) {
                            where.append(columnName);
                            where.append(" = ");
                            where.append(id);
                        } else if (type == String.class) {
                            where.append(columnName);
                            where.append(" = '");
                            where.append(id);
                            where.append("' ");
                        } else if (type == Date.class) {
                            where.append(columnName);
                            where.append(" = '");
                            where.append(id);
                            where.append("' ");
                        } else {
                            where.append(columnName);
                            where.append(" = '");
                            where.append(id);
                            where.append("'");
                        }
                        break;
                    } else {
                        continue;
                    }
                }
            }
            sqlBuilder.append(where);
        } catch (Exception e) {
            e.printStackTrace();
            sqlBuilder = null;
        }
        return sqlBuilder == null ? null : sqlBuilder.toString();
    }

    public static String getDelSQLByWhere(Class<?> clazz, String _where) {
        StringBuilder sqlBuilder = null;
        StringBuilder whereBuilder = null;
        try {
            sqlBuilder = new StringBuilder("delete from ");
            whereBuilder = new StringBuilder(" where ").append(_where);
            Table table = clazz.getAnnotation(Table.class);
            String tableName = table.name();
            if (tableName.equals("default")) {
                tableName = clazz.getSimpleName();
            }
            sqlBuilder.append(tableName).append(whereBuilder);
        } catch (Exception e) {
            e.printStackTrace();
            sqlBuilder = null;
        }
        return sqlBuilder == null ? null : sqlBuilder.toString();
    }

    public static String getDropSql(Class<?> clazz) {
        String tableName = ReflectUtil.getTableName(clazz);
        return new StringBuilder("DROP TABLE ").append(tableName).toString();
    }


    /**
     * 根据主键批量删除sql
     *
     * @param clazz
     * @param ids
     * @param isInverse true正选，false反选
     * @return
     */
    public static String getDelsSQL(Class<?> clazz, List<Object> ids, boolean isInverse) {
        StringBuilder sqlBuilder = null;
        StringBuilder where = null;
        try {
            sqlBuilder = new StringBuilder("delete from ");
            where = new StringBuilder(" where ");
            String tableName = ReflectUtil.getTableName(clazz);
            if (isInverse) {
                if (ids == null || ids.size() == 0) {
                    return sqlBuilder.append(tableName).toString();
                }
            }
            sqlBuilder.append(tableName);
            List<Field> fields = ReflectUtil.getIterationFields(clazz);
            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                if (field.isAnnotationPresent(Column.class)) {
                    Column column = field.getAnnotation(Column.class);
                    Boolean columnPKey = column.isPKey();// 主键
                    String columnName = ReflectUtil.getColumnName(field);
                    Class<?> type = field.getType();// 获取属性类型
                    if (columnPKey) {
                        where.append(columnName);
                        if (!isInverse) {
                            where.append(" in (");
                        } else {
                            where.append(" not in (");
                        }
                        for (int j = 0; j < ids.size(); j++) {
                            if (type == Integer.class) {
                                where.append(ids.get(j));
                                where.append(",");
                            } else if (type == Double.class) {
                                where.append(ids.get(j));
                                where.append(",");
                            } else if (type == Float.class) {
                                where.append(ids.get(j));
                                where.append(",");
                            } else if (type == Long.class) {
                                where.append(ids.get(j));
                                where.append(",");
                            } else if (type == Short.class) {
                                where.append(ids.get(j));
                                where.append(",");
                            } else if (type == String.class) {
                                where.append("'");
                                where.append(ids.get(j));
                                where.append("',");
                            } else if (type == Date.class) {
                                where.append("'");
                                where.append(ids.get(j));
                                where.append("',");
                            } else {
                                where.append("'");
                                where.append(ids.get(j));
                                where.append("',");
                            }
                        }
                        break;
                    } else {
                        continue;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            sqlBuilder = null;
        }
        if (!where.toString().endsWith(","))
            return null;
        int endValues = where.lastIndexOf(",");
        where.delete(endValues, endValues + 1);
        where.append(")");
        sqlBuilder.append(where);
        return sqlBuilder == null ? null : sqlBuilder.toString();
    }


    /**
     * 删除一条数据的sql,(指定where的多关键字)
     *
     * @param clazz 实体
     * @param map   指定的删除条件组合(简单的"=" 条件，其他条件不行！！！！)
     * @return
     */
    public static String getDelSQLByMultiCondition(Class<?> clazz, Map<String, Object> map) {
        StringBuilder sqlBuilder = null;
        StringBuilder where = null;

        try {
            sqlBuilder = new StringBuilder("delete from ");
            where = new StringBuilder(" where ");
            String tableName = ReflectUtil.getTableName(clazz);
            sqlBuilder.append(tableName);
            List<Field> fields = ReflectUtil.getIterationFields(clazz);
            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                if (field.isAnnotationPresent(Column.class)) {
                    String columnName = ReflectUtil.getColumnName(field);
                    String getMethodName = ReflectUtil.getMethodName(field);
                    Method getMethod = clazz.getMethod(getMethodName, new Class[]{});
                    Class<?> type = field.getType();//获取属性类型
                    if (map != null) {
                        for (String key : map.keySet()) {
                            String w = key;
                            Object v = map.get(w);
                            if (columnName.equals(w)) {
                                if (type == Date.class) { //属性按格式转换
                                    if (getMethod.isAnnotationPresent(Format.class)) { //如果指定了 日期的转换格式则执行转换
                                        Format format = getMethod.getAnnotation(Format.class);
                                        String pattern = format.dateTimePattern();
                                        v = DateUtil.getDAT(pattern, (Date) v);
                                    } else {
                                        v = DateUtil.getDAT("yyyy-MM-dd HH:mm:ss", (Date) v);
                                    }
                                }
                                where.append(TransferUtil.objectToSqlKeyValStr(type, columnName, v));
                                where.append(" and ");
                            }
                        }

                    }
                }
            }
            where.delete(where.length() - 5, where.length());
            sqlBuilder.append(where);
        } catch (Exception e) {
            e.printStackTrace();
            sqlBuilder = null;
        }
        return sqlBuilder == null ? null : sqlBuilder.toString();
    }
}
