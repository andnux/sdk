package top.andnux.http.cookie;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import top.andnux.http.utils.Utils;

public class XmlCookieStorage implements CookieStorage {

    private Application mApplication;
    private SharedPreferences mPreferences;

    public XmlCookieStorage() {
        mApplication = Utils.getApp();
        if (mApplication != null) {
            mPreferences = mApplication.getSharedPreferences("cookie", Context.MODE_PRIVATE);
        }
    }


    @Override
    public void put(String url, String cookie) {
        if (mPreferences == null) return;
        SharedPreferences.Editor edit = mPreferences.edit();
        edit.putString(Utils.md5(url), cookie);
        edit.apply();
    }

    @Override
    public String get(String url) {
        if (mPreferences == null) return null;
        return mPreferences.getString(Utils.md5(url), "");
    }
}
