package top.andnux.http.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Utils {

    private static Application mApplication;

    public static String inputStream2String(InputStream input) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));
        boolean firstLine = true;
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            if (!firstLine) {
                stringBuilder.append(System.getProperty("line.separator"));
            } else {
                firstLine = false;
            }
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

    public static String md5(String string) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(string.getBytes());
            byte[] b = md.digest();
            int i;
            StringBuilder buf = new StringBuilder("");
            for (byte b1 : b) {
                i = b1;
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static String encode(String content) {
        return Base64.encodeToString(content.getBytes(StandardCharsets.UTF_8),
                Base64.DEFAULT);
    }

    public static String decode(String content) {
        byte[] decode = Base64.decode(content.getBytes(StandardCharsets.UTF_8),
                Base64.DEFAULT);
        return new String(decode);
    }

    /**
     * 设置Cookie
     *
     * @param url
     */
    public static void setCookies(Context context, String url) {
        CookieManager cookieManager = CookieManager.getInstance();
        String cookieStr = cookieManager.getCookie(url);
        if (!TextUtils.isEmpty(cookieStr)) {
            String[] arrayCookies = cookieStr.split(";");
            if (arrayCookies.length > 0) {
                for (String cookie : arrayCookies) {
                    synCookies(context, url, cookie);
                }
            }
        }
    }

    /**
     * 同步Cookie
     *
     * @param url
     * @param cookie 格式：uid=21233 如需设置多个，需要多次调用
     */
    public static void synCookies(String url, String cookie) {
        try {
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setCookie(url, cookie);//cookies是在HttpClient中获得的cookie
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cookieManager.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置Cookie
     *
     * @param context
     * @param url
     * @param cookie  格式：uid=21233 如需设置多个，需要多次调用
     */
    public static void synCookies(Context context, String url, String cookie) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(url, cookie);//cookies格式自定义
        CookieSyncManager.getInstance().sync();
    }

    public static String getCookie(Context context, String url) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        return cookieManager.getCookie(url);
    }

    /**
     * 清除Cookie
     *
     * @param context
     */
    public static void removeCookie(Context context) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        CookieSyncManager.getInstance().sync();
    }

    public static Application getApp() {
        if (mApplication == null) {
            mApplication = getApplicationInner();
        }
        return mApplication;
    }

    private static Application getApplicationInner() {
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
}
