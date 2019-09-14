package top.andnux.utils.sqlite;

import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;

import top.andnux.utils.sqlite.annotation.Entity;
import top.andnux.utils.sqlite.annotation.Property;

class Utils {

    static String getCreateTable(Class<?> clazz) {
        StringBuilder sb = new StringBuilder();
        sb.append("create table if not exists ")
                .append(getTableName(clazz))
                .append("( ");
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);// 设置权限
            String type = field.getType().getSimpleName();
            sb.append(getFieldName(field))
                    .append(getColumnType(type))
                    .append(getPrimaryKey(field))
                    .append(getAutoincrement(field))
                    .append(", ");
        }
        sb.replace(sb.length() - 2, sb.length(), ")");
        String createTableSql = sb.toString();
        Log.e("SQL", "表语句--> " + createTableSql);
        return sb.toString();
    }

    static String getAutoincrement(Field field) {
        String s = "";
        Property annotation = field.getAnnotation(Property.class);
        if (annotation != null && annotation.autoincrement()) {
            s = " autoincrement ";
        }
        return s;
    }

    static String getPrimaryKey(Field field) {
        String s = "";
        Property annotation = field.getAnnotation(Property.class);
        if (annotation != null && annotation.primaryKey()) {
            s = " primary key ";
        }
        return s;
    }

    static String getFieldName(Field field) {
        String name = field.getName();
        Property annotation = field.getAnnotation(Property.class);
        if (annotation != null) {
            String value = annotation.value();
            if (!TextUtils.isEmpty(value)) {
                name = value;
            }
        }
        return name;
    }


    static String getTableName(Class<?> clazz) {
        Entity annotation = clazz.getAnnotation(Entity.class);
        String tableName = clazz.getSimpleName();
        if (annotation != null) {
            String value = annotation.value();
            if (!TextUtils.isEmpty(value)) {
                tableName = value;
            }
        }
        return tableName;
    }

    static String getColumnType(String type) {
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

    @SuppressWarnings("all")
    public static <T> T[] asArray(List<T> list) {
        Object[] data = new Object[list.size()];
        for (int i = 0; i < list.size(); i++) {
            data[i] = list.get(i);
        }
        return (T[]) data;
    }

    static String capitalize(String string) {
        if (!TextUtils.isEmpty(string)) {
            return string.substring(0, 1).toUpperCase(Locale.US) + string.substring(1);
        }
        return string == null ? null : "";
    }
}
