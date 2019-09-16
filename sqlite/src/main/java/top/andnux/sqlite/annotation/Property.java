package top.andnux.sqlite.annotation;

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

    boolean notNull() default false;

    boolean unique() default false;

    String defaultValue() default "";

    String check() default "";

    String value() default "";
}
