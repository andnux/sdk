package top.andnux.utils.common;

import android.util.Log;

import top.andnux.utils.BuildConfig;


public class LogUtil {

    public static String tag = "andnux";

    public static void e(String msg) {
        if (BuildConfig.DEBUG && StringUtil.isNotEmpty(msg)) {
            Log.e(tag, msg);
        }
    }

    public static void w(String msg) {
        if (BuildConfig.DEBUG && StringUtil.isNotEmpty(msg)) {
            Log.w(tag, msg);
        }
    }

    public static void i(String msg) {
        if (BuildConfig.DEBUG && StringUtil.isNotEmpty(msg)) {
            Log.i(tag, msg);
        }
    }

    public static void d(String msg) {
        if (BuildConfig.DEBUG && StringUtil.isNotEmpty(msg)) {
            Log.d(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (BuildConfig.DEBUG && StringUtil.isNotEmpty(msg)) {
            Log.e(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (BuildConfig.DEBUG && StringUtil.isNotEmpty(msg)) {
            Log.w(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (BuildConfig.DEBUG && StringUtil.isNotEmpty(msg)) {
            Log.i(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG && StringUtil.isNotEmpty(msg)) {
            Log.d(tag, msg);
        }
    }
}
