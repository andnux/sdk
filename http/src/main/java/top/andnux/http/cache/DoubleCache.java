package top.andnux.http.cache;

import android.content.Context;

public class DoubleCache extends AbstractCache {

    private Cache mMemCache;
    private Cache mSQCache;

    public DoubleCache(Context context) {
        super(context);
        mMemCache = new MemoryCache(context);
        mSQCache = new SQLiteCache(context);
    }

    @Override
    public void put(String url, String value, long time) {
        mMemCache.put(url, value, time);
        mSQCache.put(url, value, time);
    }

    @Override
    public String get(String url) {
        String s = mMemCache.get(url);
        if (s == null || "".equals(s)) {
            s = mSQCache.get(url);
        }
        return s;
    }

    @Override
    public void remove(String url) {
        mMemCache.remove(url);
        mSQCache.remove(url);
    }

    @Override
    public void clear() {
        mMemCache.clear();
        mSQCache.clear();
    }
}
