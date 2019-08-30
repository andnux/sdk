package top.andnux.utils.netstate.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import top.andnux.utils.netstate.NetState;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NetSupport {

    NetState value() default NetState.AUTO;
}
