package top.andnux.language;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

public class LocalManageUtil {

    private static final String TAG = "LocalManageUtil";

    /**
     * 获取系统的locale
     *
     * @return Locale对象
     */
    public static Locale getSystemLocale(Context context) {
        return SPUtil.getInstance(context).getSystemCurrentLocal();
    }

    /**
     * 获取选择的语言设置
     *
     * @param context
     * @return
     */
    public static LanguageBean getSetLanguageLocale(Context context) {
        return SPUtil.getInstance(context).getSelectLanguage();
    }


    public static void saveSystemCurrentLanguage(Context context) {
        SPUtil.getInstance(context).setSystemCurrentLocal(LanguageManager.getSystemLocal(context));
    }

    /**
     * 保存系统语言
     *
     * @param context
     * @param newConfig
     */
    public static void saveSystemCurrentLanguage(Context context, Configuration newConfig) {

        SPUtil.getInstance(context).setSystemCurrentLocal(LanguageManager.getSystemLocal(newConfig));
    }

    public static void saveSelectLanguage(Context context,LanguageBean bean) {
        SPUtil.getInstance(context).saveLanguage(bean);
        LanguageManager.setApplicationLanguage(context);
    }
}
