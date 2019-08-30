package top.andnux.utils.storage;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.ArrayMap;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;

public class StorageManager {

    private static final StorageManager ourInstance = new StorageManager();

    public static StorageManager getInstance() {
        return ourInstance;
    }

    private Class<? extends StorageDao> mStorageDaoClazz;
    private static final Map<String, StorageDao<?>> STRING_DAO_ARRAY_MAP = new ArrayMap<>();
    private Application mContext;

    private StorageManager() {

    }

    private Application getApplicationInner() {
        try {
            @SuppressLint("PrivateApi")
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Method currentApplication = activityThread.getDeclaredMethod("currentApplication");
            Method currentActivityThread = activityThread.getDeclaredMethod("currentActivityThread");
            Object current = currentActivityThread.invoke((Object) null);
            Object app = currentApplication.invoke(current);
            return (Application) app;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void init(Application context) {
        StorageManager instance = getInstance();
        instance.mContext = context;
    }

    public void setStorageDaoClazz(Class<? extends StorageDao> storageDaoClazz) {
        mStorageDaoClazz = storageDaoClazz;
    }

    public <T> StorageDao<T> getDao(Class<T> clazz) {
        if (mContext == null) {
            mContext = getApplicationInner();
        }
        if (mStorageDaoClazz == null) {
            mStorageDaoClazz = PreStorageDao.class;
        }
        String name = clazz.getCanonicalName();
        StorageDao<?> storageDao = STRING_DAO_ARRAY_MAP.get(name);
        if (storageDao == null) {
            try {
                Constructor<? extends StorageDao> constructor =
                        mStorageDaoClazz.getConstructor(Context.class, Class.class);
                storageDao = constructor.newInstance(mContext, clazz);
                if (name != null) {
                    STRING_DAO_ARRAY_MAP.put(name, storageDao);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return (StorageDao<T>) storageDao;
    }
}
