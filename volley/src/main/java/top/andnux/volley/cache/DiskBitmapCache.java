package top.andnux.volley.cache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import top.andnux.volley.toolbox.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class DiskBitmapCache implements ImageLoader.ImageCache {

    private DiskLruCache mDiskLruCache;

    public DiskBitmapCache(Context context) {
        try {
            mDiskLruCache = DiskLruCache.open(getDiskCacheDir(context)
                    , getAppVersion(context), 1, 100 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    private File getDiskCacheDir(Context context) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            File cacheDir = context.getExternalCacheDir();
            if (cacheDir != null) {
                cachePath = cacheDir.getAbsolutePath();
            } else {
                cachePath = context.getCacheDir().getPath();
            }
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + "image");
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(0xFF & aByte);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    private String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    @Override
    public Bitmap getBitmap(String url) {
        if (mDiskLruCache == null) return null;
        try {
            DiskLruCache.Snapshot snapShot = mDiskLruCache.get(hashKeyForDisk(url));
            if (snapShot != null) {
                InputStream is = snapShot.getInputStream(0);
                return BitmapFactory.decodeStream(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        if (mDiskLruCache == null) return;
        try {
            DiskLruCache.Editor editor = mDiskLruCache.edit(hashKeyForDisk(url));
            if (editor != null) {
                OutputStream outputStream = editor.newOutputStream(0);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                baos.writeTo(outputStream);
                editor.commit();
            }
            mDiskLruCache.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
