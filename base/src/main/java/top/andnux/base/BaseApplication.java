package top.andnux.base;

import android.app.Application;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationManager instance = ApplicationManager.getInstance();
        instance.setApplication(this);
    }
}
