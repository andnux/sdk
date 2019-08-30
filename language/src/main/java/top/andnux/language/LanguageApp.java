package top.andnux.language;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;

import java.util.Locale;

public class LanguageApp extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        //第一次进入app时保存系统选择语言(为了选择随系统语言时使用，如果不保存，切换语言后就拿不到了）
        LocalManageUtil.saveSystemCurrentLanguage(base);
        super.attachBaseContext(LanguageManager.setLocal(base));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //用户在系统设置页面切换语言时保存系统选择语言(为了选择随系统语言时使用，如果不保存，切换语言后就拿不到了）
        LocalManageUtil.saveSystemCurrentLanguage(getApplicationContext(), newConfig);
        LanguageManager.onConfigurationChanged(getApplicationContext());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LanguageManager.init(new LanguageLocalListener() {
            @Override
            public Locale getSetLanguageLocale(Context context) {
                //返回自己本地保存选择的语言设置
                return LocalManageUtil.getSetLanguageLocale(context).getLocale();
            }

            @Override
            public void reStart(Context context) {
                Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(
                        getBaseContext().getPackageName());
                    //与正常页面跳转一样可传递序列化数据,在Launch页面内获得
                if (intent != null) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("REBOOT", "reboot");
                    startActivity(intent);
                }
            }
        });
        LanguageManager.setApplicationLanguage(this);
    }
}
