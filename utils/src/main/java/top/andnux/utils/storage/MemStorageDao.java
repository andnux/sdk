package top.andnux.utils.storage;

import android.content.Context;
import android.util.LruCache;

public class MemStorageDao<T> extends BaseStorageDao<T> {

    private LruCache<String, T> mCache;

    @Override
    protected void init() {
    }

    public MemStorageDao(Context context, Class<T> clazz) {
        super(context, clazz);
        long size = Runtime.getRuntime().freeMemory() / 8;
        mCache = new LruCache<>((int) size);
    }

    @Override
    public void save(T data) throws Exception {
        mCache.put(fileName, data);
    }

    @Override
    public T load() throws Exception {
        return mCache.get(fileName);
    }

    @Override
    public void clear() {
        mCache.remove(fileName);
    }
}
