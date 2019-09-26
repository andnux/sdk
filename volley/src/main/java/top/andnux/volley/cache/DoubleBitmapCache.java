package top.andnux.volley.cache;

import android.content.Context;
import android.graphics.Bitmap;

import top.andnux.volley.toolbox.ImageLoader;

public final class DoubleBitmapCache implements ImageLoader.ImageCache {

    private ImageLoader.ImageCache mDiskBitmapCache;
    private ImageLoader.ImageCache mMemoryBitmapCache;

    public DoubleBitmapCache(Context context) {
        mMemoryBitmapCache = new MemoryBitmapCache();
        mDiskBitmapCache = new DiskBitmapCache(context);
    }

    @Override
    public Bitmap getBitmap(String url) {
        Bitmap bitmap = mMemoryBitmapCache.getBitmap(url);
        if (bitmap == null) {
            bitmap = mDiskBitmapCache.getBitmap(url);
        }
        return bitmap;
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        mMemoryBitmapCache.putBitmap(url,bitmap);
        mDiskBitmapCache.putBitmap(url,bitmap);
    }
}
