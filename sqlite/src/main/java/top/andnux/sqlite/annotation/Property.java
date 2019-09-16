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

    /**
     * 自增长
     *
     * @return
     */
    boolean autoincrement() default false;

    /**
     * 主键
     *
     * @return
     */
    boolean primaryKey() default false;

    /**
     * 不能为空
     *
     * @return
     */
    boolean notNull() default false;

    /**
     * 唯一值
     *
     * @return
     */
    boolean unique() default false;

    /**
     * 默认值
     *
     * @return
     */
    String defaultValue() default "";

    /**
     * 检查器
     *
     * @return
     */
    String check() default "";

    /**
     * 列名
     *
     * @return
     */
    String value() default "";

    /**
     * 可选的列名
     *
     * @return
     */
    String optional() default "";
}
