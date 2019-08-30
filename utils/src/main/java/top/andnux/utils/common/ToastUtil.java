package top.andnux.utils.common;

import android.content.Context;

import androidx.annotation.NonNull;

import es.dmoral.toasty.Toasty;
import top.andnux.utils.Utils;

public class ToastUtil {

    //成功
    public static void success(@NonNull String message) {
        success(Utils.getApp(), message, Toasty.LENGTH_SHORT);
    }

    //成功
    public static void success(@NonNull Context context, @NonNull String message) {
        success(context, message, Toasty.LENGTH_SHORT);
    }

    //成功
    public static void success(@NonNull Context context, @NonNull String message, int duration) {
        if (StringUtil.isNotEmpty(message)) {
            Toasty.success(context, message, duration).show();
        }
    }

    //感叹号
    public static void info(@NonNull String message) {
        if (StringUtil.isNotEmpty(message)) {
            Toasty.info(Utils.getApp(), message, Toasty.LENGTH_SHORT).show();
        }
    }


    //感叹号
    public static void info(@NonNull Context context, @NonNull String message) {
        if (StringUtil.isNotEmpty(message)) {
            Toasty.info(context, message, Toasty.LENGTH_SHORT).show();
        }
    }

    //感叹号
    public static void info(@NonNull Context context, @NonNull String message, int duration) {
        if (StringUtil.isNotEmpty(message)) {
            Toasty.info(context, message, duration).show();
        }
    }

    //正常
    public static void normal(@NonNull String message) {
        if (StringUtil.isNotEmpty(message)) {
            Toasty.normal(Utils.getApp(), message, Toasty.LENGTH_SHORT).show();
        }
    }

    //正常
    public static void normal(@NonNull Context context, @NonNull String message) {
        if (StringUtil.isNotEmpty(message)) {
            Toasty.normal(context, message, Toasty.LENGTH_SHORT).show();
        }
    }

    //正常
    public static void normal(@NonNull Context context, @NonNull String message, int duration) {
        if (StringUtil.isNotEmpty(message)) {
            Toasty.normal(context, message, duration).show();
        }
    }

    //警告
    public static void warning(@NonNull String message) {
        if (StringUtil.isNotEmpty(message)) {
            Toasty.warning(Utils.getApp(), message, Toasty.LENGTH_SHORT).show();
        }
    }

    //警告
    public static void warning(@NonNull Context context, @NonNull String message) {
        if (StringUtil.isNotEmpty(message)) {
            Toasty.warning(context, message, Toasty.LENGTH_SHORT).show();
        }
    }

    //警告
    public static void warning(@NonNull Context context, @NonNull String message, int duration) {
        if (StringUtil.isNotEmpty(message)) {
            Toasty.warning(context, message, duration).show();
        }
    }

    //错误
    public static void error(@NonNull String message) {
        if (StringUtil.isNotEmpty(message)) {
            Toasty.error(Utils.getApp(), message, Toasty.LENGTH_SHORT).show();
        }
    }

    //错误
    public static void error(@NonNull Context context, @NonNull String message) {
        if (StringUtil.isNotEmpty(message)) {
            Toasty.error(context, message, Toasty.LENGTH_SHORT).show();
        }
    }

    //错误
    public static void error(@NonNull Context context, @NonNull String message, int duration) {
        if (StringUtil.isNotEmpty(message)) {
            Toasty.error(context, message, duration).show();
        }
    }
}
