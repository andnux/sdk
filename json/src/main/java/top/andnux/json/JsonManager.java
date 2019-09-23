package top.andnux.json;

import java.lang.reflect.Type;
import java.util.List;

public class JsonManager {

    private static final JsonManager ourInstance = new JsonManager();

    public static JsonManager getInstance() {
        return ourInstance;
    }

    private JsonProxy mJsonAdapter = new FastJsonProxy();

    private JsonManager() {

    }

    public void setJsonAdapter(JsonProxy jsonAdapter) {
        mJsonAdapter = jsonAdapter;
    }

    public JsonProxy getJsonAdapter() {
        return mJsonAdapter;
    }

    /**
     * 转换对接为json
     *
     * @param o
     * @return
     */
    public String toJSONString(Object o) {
        if (mJsonAdapter == null) mJsonAdapter = new FastJsonProxy();
        return mJsonAdapter.toJSONString(o);
    }

    /**
     * 解析json对象
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T parseObject(String json, Class<T> clazz) {
        if (mJsonAdapter == null) mJsonAdapter = new FastJsonProxy();
        return mJsonAdapter.parseObject(json, clazz);
    }

    /**
     * 解析json对象
     *
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    public <T> T parseObject(String json, Type type) {
        if (mJsonAdapter == null) mJsonAdapter = new FastJsonProxy();
        return mJsonAdapter.parseObject(json, type);
    }

    /**
     * 解析json对象数组
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> parseArray(String json, Class<T> clazz) {
        if (mJsonAdapter == null) mJsonAdapter = new FastJsonProxy();
        return mJsonAdapter.parseArray(json, clazz);
    }

    /**
     * 解析json对象数组
     *
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    public <T> List<T> parseArray(String json, Type type) {
        if (mJsonAdapter == null) mJsonAdapter = new FastJsonProxy();
        return mJsonAdapter.parseArray(json, type);
    }
}
