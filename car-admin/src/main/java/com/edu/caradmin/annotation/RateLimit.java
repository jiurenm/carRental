package com.edu.caradmin.annotation;

import java.lang.annotation.*;

/**
 * 限流
 *
 * @author Administrator
 * @date 2019/1/22 10:30
 */
@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {
    int limitNum() default 20;
}
