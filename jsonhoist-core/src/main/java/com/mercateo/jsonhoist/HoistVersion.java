package com.mercateo.jsonhoist;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to be used to declare version information on a root-class.
 *
 * @author usr
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HoistVersion {

    String NO_TYPE = "__NO_TYPE";

    long value();

    String type() default NO_TYPE;

}
