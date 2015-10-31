package me.liuyichen.viewinjector.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by liuchen on 15/10/30.
 * and ...
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface InjectView {

    int value();
}
