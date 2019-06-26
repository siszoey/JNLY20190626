package com.lib.bandaid.data.local.sqlite.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zy on 2017/1/19.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Format {

    /**
     * 日期格式样式
     * @return
     */
    String dateTimePattern() default "yyyy-MM-dd HH:mm:ss";
}
