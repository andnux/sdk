package top.andnux.net.cookie;

import java.util.HashMap;
import java.util.Map;

import top.andnux.net.utils.Utils;

public class MemoryCookieStorage implements CookieStorage {

    private Map<String, String> mCache;

    public MemoryCookieStorage() {
        mCache = new HashMap<>();
    }

    @Override
    public void put(String url, String cookie) {
        mCache.put(Utils.md5(url), cookie);
    }

    @Override
    public String get(String url) {
        return mCache.get(Utils.md5(url));
    }
}
