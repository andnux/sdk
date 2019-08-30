package top.andnux.language;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;

import java.util.Locale;

public class SPUtil {

    private final String SP_NAME = "language_setting";
    private final String TAG_LANGUAGE = "language_select";
    private final String TAG_SYSTEM_LANGUAGE = "system_language";
    private static volatile SPUtil instance;
    private final SharedPreferences mSharedPreferences;

    private Locale systemCurrentLocal = Locale.ENGLISH;

    public SPUtil(Context context) {
        mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }


    public void saveLanguage(LanguageBean bean) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        String jsonString = JSON.toJSONString(bean);
        edit.putString(TAG_LANGUAGE, jsonString);
        edit.apply();
    }

    public LanguageBean getSelectLanguage() {
        String jsonString = mSharedPreferences.getString(TAG_LANGUAGE, "");
        LanguageBean languageBean = new LanguageBean("auto", "跟随系统", getSystemCurrentLocal());
        try {
            LanguageBean tmp = JSON.parseObject(jsonString, LanguageBean.class);
            if (tmp == null || "auto".equalsIgnoreCase(tmp.getCode())) {
                //第一次安装
                return languageBean;
            } else {
                languageBean = tmp;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return languageBean;
    }


    public Locale getSystemCurrentLocal() {
        return systemCurrentLocal;
    }

    public void setSystemCurrentLocal(Locale local) {
        systemCurrentLocal = local;
    }

    public static SPUtil getInstance(Context context) {
        if (instance == null) {
            synchronized (SPUtil.class) {
                if (instance == null) {
                    instance = new SPUtil(context);
                }
            }
        }
        return instance;
    }
}
