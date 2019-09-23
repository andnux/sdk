package top.andnux.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class GoogleJsonProxy implements JsonProxy {

    private Gson mGson = new Gson();

    @Override
    public String toJSONString(Object o) {
        return mGson.toJson(o);
    }

    @Override
    public <T> T parseObject(String json, Class<T> clazz) {
        return mGson.fromJson(json, clazz);
    }

    @Override
    public <T> T parseObject(String json, Type type) {
        return mGson.fromJson(json, type);
    }

    @Override
    public <T> List<T> parseArray(String json, Class<T> clazz) {
        return mGson.fromJson(json, new TypeToken<List<T>>() {
        }.getType());
    }

    @Override
    public <T> List<T> parseArray(String json, Type type) {
        return mGson.fromJson(json, new TypeToken<List<T>>() {
        }.getType());
    }
}
