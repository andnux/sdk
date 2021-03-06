package top.andnux.sqlite;

import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;

import top.andnux.sqlite.annotation.Entity;
import top.andnux.sqlite.annotation.Property;
import top.andnux.sqlite.annotation.Trigger;

public class Support {

    public static String createTable(Class<?> clazz, boolean ifNotExits) {
        StringBuilder sb = new StringBuilder();
        String ifNotExitsSql = ifNotExits ? "if not exists " : "";
        sb.append("create table ").append(ifNotExitsSql)
                .append(getTableName(clazz))
                .append("( ");
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);// 设置权限
            String type = field.getType().getSimpleName();
            String fieldName = getFieldName(field);
            if (!TextUtils.isEmpty(fieldName)) {
                sb.append(fieldName)
                        .append(getColumnType(type))
                        .append(getPrimaryKey(field))
                        .append(getNotNull(field))
                        .append(getUnique(field))
                        .append(getCheck(field))
                        .append(getDefaultValue(field))
                        .append(getAutoincrement(field))
                        .append(", ");
            }

        }
        sb.replace(sb.length() - 2, sb.length(), ")");
        String createTableSql = sb.toString();
        Log.e("SQL", "表语句--> " + createTableSql);
        return sb.toString();
    }

    public static String createTrigger(Class<?> clazz) {
        StringBuilder sb = new StringBuilder();
        String name = clazz.getSimpleName();
        Trigger trigger = clazz.getAnnotation(Trigger.class);
        if (trigger == null) {
            return "";
        }
        if (!TextUtils.isEmpty(trigger.value())) {
            name = trigger.value();
        }
        if (TextUtils.isEmpty(name)) {
            return "";
        }
        sb.append("create trigger ").append(name)
                .append(" ")
                .append(trigger.type().name())
                .append(" ")
                .append(trigger.event().name())
                .append(" on ")
                .append(trigger.on())
                .append(" begin ")
                .append(trigger.body())
                .append(" end ");
        Log.e("SQL", "触发器--> " + sb.toString());
        return sb.toString();
    }

    public static String getAutoincrement(Field field) {
        String s = "";
        Property annotation = field.getAnnotation(Property.class);
        if (annotation != null && annotation.autoincrement()) {
            s = " autoincrement ";
        }
        return s;
    }

    public static String getPrimaryKey(Field field) {
        String s = "";
        Property annotation = field.getAnnotation(Property.class);
        if (annotation != null && annotation.primaryKey()) {
            s = " primary key ";
        }
        return s;
    }

    public static String getNotNull(Field field) {
        String s = "";
        Property annotation = field.getAnnotation(Property.class);
        if (annotation != null && annotation.notNull()) {
            s = " not null ";
        }
        return s;
    }

    public static String getUnique(Field field) {
        String s = "";
        Property annotation = field.getAnnotation(Property.class);
        if (annotation != null && annotation.unique()) {
            s = " unique ";
        }
        return s;
    }


    public static String getDefaultValue(Field field) {
        String s = "";
        Property annotation = field.getAnnotation(Property.class);
        if (annotation != null && !TextUtils.isEmpty(annotation.defaultValue())) {
            s = " default '" + annotation.defaultValue() + "' ";
        }
        return s;
    }

    public static String getCheck(Field field) {
        String s = "";
        Property annotation = field.getAnnotation(Property.class);
        if (annotation != null && !TextUtils.isEmpty(annotation.check())) {
            s = " check( " + annotation.check() + " ) ";
        }
        return s;
    }

    public static String getFieldName(Field field) {
        String name = "";
        Property annotation = field.getAnnotation(Property.class);
        if (annotation != null) {
            String value = annotation.value();
            if (!TextUtils.isEmpty(value)) {
                name = value;
            } else {
                name = field.getName();
            }
        }
        return name;
    }

    public static String getFieldNameOptional(Field field) {
        String name = "";
        Property annotation = field.getAnnotation(Property.class);
        if (annotation != null) {
            String value = annotation.optional();
            if (!TextUtils.isEmpty(value)) {
                name = value;
            } else {
                name = field.getName();
            }
        }
        return name;
    }



    public static String getTableName(Class<?> clazz) {
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

    @SuppressWarnings("all")
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
