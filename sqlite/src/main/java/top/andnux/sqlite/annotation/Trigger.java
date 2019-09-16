package top.andnux.sqlite.annotation;

import androidx.annotation.Keep;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import top.andnux.sqlite.trigger.Event;
import top.andnux.sqlite.trigger.Type;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Keep
public @interface Trigger {

    String value() default "";

    Type type() default Type.NONE;

    String body() default "";

    Event event() default Event.NONE;

    String on() default "";

    String of() default "";
}
