package top.andnux.base;

import android.annotation.SuppressLint;
import android.app.Application;

import java.lang.reflect.Method;

public class ApplicationManager {

    private static final ApplicationManager ourInstance = new ApplicationManager();

    public static ApplicationManager getInstance() {
        return ourInstance;
    }

    private Application mApplication;

    private ApplicationManager() {

    }

    public Application getApplication() {
        if (mApplication == null){
            mApplication = getApplicationInner();
        }
        return mApplication;
    }

    public void setApplication(Application application) {
        mApplication = application;
    }

    @SuppressLint("all")
    private static Application getApplicationInner() {
        try {
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
