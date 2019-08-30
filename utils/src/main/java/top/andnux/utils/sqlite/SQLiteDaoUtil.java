package top.andnux.utils.sqlite;

import android.text.TextUtils;

import java.util.List;
import java.util.Locale;

public class SQLiteDaoUtil {

    private SQLiteDaoUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static String getTableName(Class<?> clazz) {
        return clazz.getSimpleName();
    }

    public static String getColumnType(String type) {
        String value = null;
        if (type.toLowerCase().contains("string")) {
            value = " text";
        } else if (type.toLowerCase().contains("int")) {
            value = " integer";
        } else if (type.toLowerCase().contains("boolean")) {
            value = " boolean";
        } else if (type.toLowerCase().contains("float")) {
            value = " float";
        } else if (type.toLowerCase().contains("double")) {
            value = " double";
        } else if (type.toLowerCase().contains("char")) {
            value = " varchar";
        } else if (type.toLowerCase().contains("long")) {
            value = " long";
        }
        return value;
    }

    public static <T> T[] asArray(List<T> list) {
        Object[] data = new Object[list.size()];
        for (int i = 0; i < list.size(); i++) {
            data[i] = list.get(i);
        }
        return (T[]) data;
    }

    public static String capitalize(String string) {
        if (!TextUtils.isEmpty(string)) {
            return string.substring(0, 1).toUpperCase(Locale.US) + string.substring(1);
        }
        return string == null ? null : "";
    }
}
