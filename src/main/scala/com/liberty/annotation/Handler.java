package com.liberty.annotation;

import com.liberty.processing.CommandActor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * User: Dimitr
 * Date: 06.01.14
 * Time: 14:45
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value= ElementType.FIELD)
public @interface Handler {
    public Class<? extends CommandActor> value();
}
