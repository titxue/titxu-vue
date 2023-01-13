package com.titxu.cloud.common.log.annotation;

import java.lang.annotation.*;

/**
 * 系统日志注解
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

    String value() default "";

    /**
     * spel 表达式
     *
     * @return 日志描述
     */
    String expression() default "";
}
