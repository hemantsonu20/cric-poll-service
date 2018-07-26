package com.github.hemantsonu20.cric.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to be used on method level. Then it will log entry / exit of the
 * method with parameters, return value or exception if any. Using this one can
 * easily trace flows from one method to another
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Loggable {

	// to disable this annotation / nothing will be logged if set to false
	boolean enable() default true;
}
