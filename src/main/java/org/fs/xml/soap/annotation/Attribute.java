package org.fs.xml.soap.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Fatih on 01/07/16.
 * as org.fs.xml.soap.annotation.Attribute
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Attribute {

    String  name()          default "";
    String  namespace()     default "";
    boolean required()      default true;

}
