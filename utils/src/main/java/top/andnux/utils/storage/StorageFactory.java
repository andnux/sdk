package top.andnux.utils.storage;

import android.content.Context;
import android.util.ArrayMap;

import java.util.Map;


@Deprecated
public class StorageFactory {

    private static final Map<String, StorageDao<?>> STRING_DAO_ARRAY_MAP = new ArrayMap<>();

    public static <T> StorageDao<T> getPreDao(Context context,Class<T> clazz) {
        StorageDao<?> dao = STRING_DAO_ARRAY_MAP.get(clazz.getSimpleName());
        if (dao == null) {
            dao = new PreStorageDao<T>(context,clazz);
            STRING_DAO_ARRAY_MAP.put(clazz.getSimpleName(), dao);
        }
        return (StorageDao<T>) dao;
    }

    public static <T> StorageDao<T> getMemDao(Context context,Class<T> clazz) {
        StorageDao<?> dao = STRING_DAO_ARRAY_MAP.get(clazz.getSimpleName());
        if (dao == null) {
            dao = new MemStorageDao<T>(context,clazz);
            STRING_DAO_ARRAY_MAP.put(clazz.getSimpleName(), dao);
        }
        return (StorageDao<T>) dao;
    }

    public static <T> StorageDao<T> getMMKVDao(Context context,Class<T> clazz) {
        StorageDao<?> dao = STRING_DAO_ARRAY_MAP.get(clazz.getSimpleName());
        if (dao == null) {
            dao = new MMKVStorageDao<>(context,clazz);
            STRING_DAO_ARRAY_MAP.put(clazz.getSimpleName(), dao);
        }
        return (StorageDao<T>) dao;
    }
}
