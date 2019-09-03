package top.andnux.json;

import java.lang.reflect.Type;
import java.util.List;

public interface JsonConverter {

    /**
     * 转换对接为json
     *
     * @param o
     * @return
     */
    String toString(Object o);

    /**
     * 解析json队长
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T parseObject(String json, Class<T> clazz);

    /**
     * 解析json对象
     *
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    <T> T parseObject(String json, Type type);

    /**
     * 解析json对象数组
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    <T> List<T> parseArray(String json, Class<T> clazz);

    /**
     * 解析json对象数组
     *
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    <T> List<T> parseArray(String json, Type type);
}
