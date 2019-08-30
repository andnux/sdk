package top.andnux.http.cache;


import java.util.HashMap;
import java.util.Map;

import top.andnux.http.utils.Utils;

public class MemoryCache implements Cache {

    private Map<String, CacheEntity> mCache;

    public MemoryCache() {
        mCache = new HashMap<>();
    }

    @Override
    public void put(String url, String value, long time) {
        CacheEntity entity = new CacheEntity();
        entity.setUrl(Utils.md5(url));
        entity.setData(value);
        entity.setTime(System.currentTimeMillis());
        entity.setDuration(time);
        mCache.put(url, entity);
    }

    @Override
    public String get(String url) {
        CacheEntity entity = mCache.get(url);
        if (entity != null) {
            long time = entity.getDuration();
            if (time < 0) {
                return entity.getData();
            } else {
                long timeMillis = System.currentTimeMillis() - entity.getTime();
                if (timeMillis > entity.getDuration()) {
                    mCache.remove(url);
                    return null;
                } else {
                    return entity.getData();
                }
            }
        }
        return null;
    }

    @Override
    public void remove(String url) {
        mCache.remove(url);
    }

    @Override
    public void clear() {
        mCache.clear();
    }
}
