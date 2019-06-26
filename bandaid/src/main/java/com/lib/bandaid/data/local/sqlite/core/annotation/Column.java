package com.lib.bandaid.data.local.sqlite.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zy on 2017/1/13.
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {
    /**
     * 对应表里的字段名称
     *
     * @return
     */
    String name() default "default";

    /**
     * 字段别名
     *
     * @return
     */
    String alias() default "default";

    /**
     * 设置对应的数据库字段类型
     *
     * @return
     */
    String type() default "default";

    /**
     * @return
     */
    boolean autoincrement() default false;

    /**
     * 是否为数据库主键
     */
    boolean isPKey() default false;

    /**
     * 增删改查 所对应的操作函数
     *
     * @return
     */
    String save() default "default";

    String del() default "default";

    String update() default "default";

    String query() default "default";
}
