package top.andnux.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.ArrayMap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import top.andnux.sqlite.annotation.Property;

public class SQLiteDaoImpl<T> implements SQLiteDao<T> {

    private SQLiteDatabase mSqLiteDatabase;
    private Class<T> mClazz;
    private static final Object[] mPutMethodArgs = new Object[2];
    private static final Map<String, Method> mPutMethods
            = new ArrayMap<>();

    SQLiteDaoImpl(SQLiteDatabase database, Class<T> clazz) {
        this.mClazz = clazz;
        mSqLiteDatabase = database;
        //创建表
        String table = Support.createTable(clazz, true);
        if (!TextUtils.isEmpty(table)) {
            mSqLiteDatabase.execSQL(table);
        }
        //创建触发器
        String trigger = Support.createTrigger(clazz);
        if (!TextUtils.isEmpty(trigger)) {
            mSqLiteDatabase.execSQL(trigger);
        }
    }


    // 插入数据库 t 是任意对象
    @Override
    public long insert(T obj) {
        ContentValues values = contentValuesByObj(obj);
        return mSqLiteDatabase.insert(Support.getTableName(mClazz),
                null, values);
    }

    @Override
    public void insert(List<T> datas) {
        // 批量插入采用 事物
        mSqLiteDatabase.beginTransaction();
        for (T data : datas) {
            // 调用单条插入
            insert(data);
        }
        mSqLiteDatabase.setTransactionSuccessful();
        mSqLiteDatabase.endTransaction();
    }

    private SQLiteQuery<T> mQuerySupport;

    @Override
    public SQLiteQuery<T> querySupport() {
        if (mQuerySupport == null) {
            mQuerySupport = new SQLiteQuery<>(mSqLiteDatabase, mClazz);
        }
        return mQuerySupport;
    }

    @Override
    public List<T> query(QueryWhere where) {
        Cursor cursor = mSqLiteDatabase.rawQuery(where.toString(), null);
        return cursorToList(cursor);
    }

    @Override
    public int update(T data, QueryWhere where) {
        ContentValues values = contentValuesByObj(data);
        return mSqLiteDatabase.update(Support.getTableName(mClazz), values,
                where.toString(), null);
    }

    @Override
    public int delete(QueryWhere where) {
        return mSqLiteDatabase.delete(Support.getTableName(mClazz), where.toString(), null);
    }

    // obj 转成 ContentValues
    private ContentValues contentValuesByObj(T obj) {
        ContentValues values = new ContentValues();
        Field[] fields = mClazz.getDeclaredFields();
        for (Field field : fields) {
            try {
                if (field.getAnnotation(Property.class) == null) {
                    continue;
                }
                // 设置权限，私有和共有都可以访问
                field.setAccessible(true);
                String key = Support.getFieldName(field);
                // 获取value
                Object value = field.get(obj);
                if (value != null) {
                    // put 第二个参数是类型  把它转换
                    mPutMethodArgs[0] = key;
                    mPutMethodArgs[1] = value;
                    String filedTypeName = field.getType().getName();
                    Method putMethod = mPutMethods.get(filedTypeName);
                    if (putMethod == null) {
                        putMethod = ContentValues.class.getDeclaredMethod("put",
                                String.class, value.getClass());
                        mPutMethods.put(filedTypeName, putMethod);
                    }
                    putMethod.invoke(values, mPutMethodArgs);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mPutMethodArgs[0] = null;
                mPutMethodArgs[1] = null;
            }
        }
        return values;
    }

    /**
     * 删除
     */
    public int delete(String whereClause, String[] whereArgs) {
        return mSqLiteDatabase.delete(Support.getTableName(mClazz), whereClause, whereArgs);
    }

    /**
     * 更新  这些你需要对  最原始的写法比较明了 extends
     */
    public int update(T obj, String whereClause, String... whereArgs) {
        ContentValues values = contentValuesByObj(obj);
        return mSqLiteDatabase.update(Support.getTableName(mClazz),
                values, whereClause, whereArgs);
    }

    /**
     * 通过Cursor封装成查找对象
     *
     * @return 对象集合列表
     */
    private List<T> cursorToList(Cursor cursor) {
        List<T> list = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                try {
                    T instance = mClazz.newInstance();
                    Field[] fields = mClazz.getDeclaredFields();
                    for (Field field : fields) {
                        // 遍历属性
                        field.setAccessible(true);
                        String name = Support.getFieldName(field);
                        // 获取角标
                        int index = cursor.getColumnIndex(name);
                        if (index == -1) { //支持可选列名
                            name = Support.getFieldNameOptional(field);
                            index = cursor.getColumnIndex(name);
                        }
                        if (index == -1) {
                            continue;
                        }
                        // 通过反射获取 游标的方法
                        Method cursorMethod = cursorMethod(field.getType());
                        Object value = cursorMethod.invoke(cursor, index);
                        if (value == null) {
                            continue;
                        }

                        // 处理一些特殊的部分
                        if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                            if ("0".equals(String.valueOf(value))) {
                                value = false;
                            } else if ("1".equals(String.valueOf(value))) {
                                value = true;
                            }
                        } else if (field.getType() == char.class || field.getType() == Character.class) {
                            value = ((String) value).charAt(0);
                        } else if (field.getType() == Date.class) {
                            long date = (Long) value;
                            if (date <= 0) {
                                value = null;
                            } else {
                                value = new Date(date);
                            }
                        }
                        field.set(instance, value);
                    }
                    // 加入集合
                    list.add(instance);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    private Method cursorMethod(Class<?> type) throws Exception {
        String methodName = getColumnMethodName(type);
        return Cursor.class.getMethod(methodName, int.class);
    }

    private String getColumnMethodName(Class<?> fieldType) {
        String typeName;
        if (fieldType.isPrimitive()) {
            typeName = Support.capitalize(fieldType.getName());
        } else {
            typeName = fieldType.getSimpleName();
        }
        String methodName = "get" + typeName;
        if ("getBoolean".equals(methodName)) {
            methodName = "getInt";
        } else if ("getChar".equals(methodName) || "getCharacter".equals(methodName)) {
            methodName = "getString";
        } else if ("getDate".equals(methodName)) {
            methodName = "getLong";
        } else if ("getInteger".equals(methodName)) {
            methodName = "getInt";
        }
        return methodName;
    }
}
