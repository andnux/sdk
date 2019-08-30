package top.andnux.utils.common;

import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Field;

public class TypefaceUtil {

    private static void setDefaultTypeface(Context context, String fieldName, String font) {
        Typeface typeFace = Typeface.createFromAsset(context.getAssets(), font);
        try {
            Field field = Typeface.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(null, typeFace);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setMonospaceTypeface(Context context, String font) {
        setDefaultTypeface(context, "MONOSPACE", font);
    }

    public static void setSerifTypeface(Context context, String font) {
        setDefaultTypeface(context, "SERIF", font);
    }

    public static void setSansSerifTypeface(Context context, String font) {
        setDefaultTypeface(context, "SANS_SERIF", font);
    }

    public static void setDefaultBoldTypeface(Context context, String font) {
        setDefaultTypeface(context, "DEFAULT_BOLD", font);
    }

    public static void setDefaultTypeface(Context context, String font) {
        setDefaultTypeface(context, "DEFAULT", font);
    }
}
