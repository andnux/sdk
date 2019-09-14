package top.andnux.utils.sqlite.annotation;

import androidx.annotation.Keep;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Keep
public @interface Property {

    boolean autoincrement() default false;

    boolean primaryKey() default false;

    String value() default "";
}
