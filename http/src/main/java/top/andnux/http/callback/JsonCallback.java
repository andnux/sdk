package top.andnux.http.callback;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;

public abstract class JsonCallback<T> extends StringCallback {

    private static final Gson GSON = new Gson();

    public abstract void onSuccess(T data);

    @Override
    public void onSuccess(String string) {
        try {
            Class<T> entityClass = (Class<T>) ((ParameterizedType)
                    getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            T t = GSON.fromJson(string, entityClass);
            onSuccess(t);
        } catch (Exception e) {
            onFail(e);
        }
    }
}
