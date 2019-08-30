package top.andnux.utils.common;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by andnux on 16/9/19.
 */
public class JsonUtil {


    private static Gson mGson = new Gson();

    public static Map<String, Object> toMap(String jsonString) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            Iterator<String> keyIter = jsonObject.keys();
            String key;
            Object value;
            Map<String, Object> valueMap = new ConcurrentHashMap<String, Object>();
            while (keyIter.hasNext()) {
                key = (String) keyIter.next();
                value = jsonObject.get(key);
                valueMap.put(key, value);
            }
            return valueMap;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T toBean(String json, Class<T> clazz) {
        try {
            return mGson.fromJson(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T jsonToBean(String json, Class<T> c) {
        try {
            return mGson.fromJson(json, c);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static <T> T jsonToBean(String json, Type type) {
        try {
            return mGson.fromJson(json, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> List<T> parseJsonArrayWithGson(String jsonData, Class<T> type) {
        try {
            List<T> result = mGson.fromJson(jsonData, new TypeToken<List<T>>() {
            }.getType());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String beanToJson(Object bean) {
        return mGson.toJson(bean);
    }
}
