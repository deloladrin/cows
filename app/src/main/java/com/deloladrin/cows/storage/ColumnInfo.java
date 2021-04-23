package com.deloladrin.cows.storage;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(FIELD)
public @interface ColumnInfo
{
    int index();
    String name();

    boolean primary() default false;
    boolean autoIncrement() default false;
}
