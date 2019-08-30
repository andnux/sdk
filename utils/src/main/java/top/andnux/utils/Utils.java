package top.andnux.utils;

import android.annotation.SuppressLint;
import android.app.Application;

import java.lang.reflect.Method;

public class Utils {

    private static Application mApplication;

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