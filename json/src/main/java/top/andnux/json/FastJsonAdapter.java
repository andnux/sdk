package top.andnux.json;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Type;
import java.util.List;

public class FastJsonAdapter implements JsonAdapter {
    @Override
    public String toJSONString(Object o) {
        return JSON.toJSONString(o);
    }

    @Override
    public <T> T parseObject(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    @Override
    public <T> T parseObject(String json, Type type) {
        return JSON.parseObject(json, type);
    }

    @Override
    public <T> List<T> parseArray(String json, Class<T> clazz) {
        return JSON.parseArray(json, clazz);
    }
    @Override
    @SuppressWarnings("all")
    public <T> List<T> parseArray(String json, Type type) {
        return (List<T>) JSON.parseArray(json, new Type[]{type});
    }
}
