package top.andnux.utils.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import top.andnux.utils.storage.annotation.KeyName;
public class PreStorageDao<T> extends BaseStorageDao<T> {

    private SharedPreferences mPreferences;

    @Override
    protected void init() { 
        mPreferences = mContext.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    public PreStorageDao(Context context, Class<T> clazz) {
        super(context, clazz);
    }

    @Override
    public void save(T data) throws Exception {
        Field[] fields = data.getClass().getDeclaredFields();
        SharedPreferences.Editor edit = mPreferences.edit();
        for (Field field : fields) {
            field.setAccessible(true);
            saveData(mPreferences, data, edit, field);
        }
        edit.apply();
    }

    protected void saveData(SharedPreferences sp, Object data,
                            SharedPreferences.Editor edit,
                            Field field) throws Exception {
        KeyName annotation = field.getAnnotation(KeyName.class);
        String name = field.getName();
        if (annotation != null && !TextUtils.isEmpty(annotation.value())) {
            name = annotation.value();
        }
        if (field.getType().equals(String.class)) {
            edit.putString(name, (String) field.get(data));
        } else if (field.getType().equals(int.class) || field.getType().equals(Integer.class)) {
            edit.putInt(name, (Integer) field.get(data));
        } else if (field.getType().equals(Long.class)) {
            edit.putLong(name, (Long) field.get(data));
        } else if (field.getType().equals(Float.class)) {
            edit.putFloat(name, (Float) field.get(data));
        } else if (field.getType().equals(boolean.class) ||
                field.getType().equals(Boolean.class)) {
            edit.putBoolean(name, (Boolean) field.get(data));
        } else if (field.getType().equals(Date.class)) {
            edit.putString(name, sdf.format(field.get(data)));
        } else if (field.getType().equals(Set.class)) {
            edit.putStringSet(name, (Set<String>) field.get(data));
        } else {
            Object o = field.get(data);
            String s = new Gson().toJson(o);
            edit.putString(name, s);
        }
    }

    @Override
    public T load() {
        T t = null;
        try {
            t = mClass.newInstance();
            Map<String, ?> all = mPreferences.getAll();
            Set<String> strings = all.keySet();
            Field[] fields = mClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                for (String string : strings) {
                    if (field.getName().equals(string)) {
                        loadData(mPreferences, t, field);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    @Override
    public void clear() {
        mPreferences.edit().clear().apply();
    }

    protected void loadData(SharedPreferences sp, Object t, Field field)
            throws Exception {
        KeyName annotation = field.getAnnotation(KeyName.class);
        String name = field.getName();
        if (annotation != null && !TextUtils.isEmpty(annotation.value())) {
            name = annotation.value();
        }
        if (field.getType().equals(String.class)) {
            String value = sp.getString(name, "");
            field.set(t, value);
        } else if (field.getType().equals(Integer.class)
                || field.getType().equals(int.class)) {
            Integer value = sp.getInt(name, 0);
            field.set(t, value);
        } else if (field.getType().equals(Float.class)) {
            Float value = sp.getFloat(name, 0);
            field.set(t, value);
        } else if (field.getType().equals(Long.class)) {
            Long value = sp.getLong(name, 0);
            field.set(t, value);
        } else if (field.getType().equals(Boolean.class) ||
                field.getType().equals(boolean.class)) {
            Boolean value = sp.getBoolean(name, false);
            field.set(t, value);
        } else if (field.getType().equals(Date.class)) {
            String value = sp.getString(name, "");
            try {
                if (!TextUtils.isEmpty(value)) {
                    Date parse = sdf.parse(value);
                    field.set(t, parse);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (field.getType().equals(Set.class)) {
            Set<String> stringSet = sp.getStringSet(name, new HashSet<>());
            field.set(t, stringSet);
        } else {
            try {
                String value = sp.getString(name, "");
                if (!TextUtils.isEmpty(value)) {
                    Object o = new Gson().fromJson(value, field.getType());
                    field.set(t, o);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
