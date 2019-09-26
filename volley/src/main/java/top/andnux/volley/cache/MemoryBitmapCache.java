package top.andnux.volley.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

import top.andnux.volley.toolbox.ImageLoader;

public final class MemoryBitmapCache implements ImageLoader.ImageCache {

    private final LruCache<String, Bitmap> mMemoryCache;

    public MemoryBitmapCache() {
        int maxMemory = (int) (Runtime.getRuntime().totalMemory() / 1024);
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
    }

    @Override
    public Bitmap getBitmap(String url) {
        return mMemoryCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        mMemoryCache.put(url, bitmap);
    }
}
