package top.andnux.utils.sqlite;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.ArrayMap;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import top.andnux.utils.Utils;

public class SQLiteDaoImpl<T> implements SQLiteDao<T> {

    private SQLiteDatabase mSqLiteDatabase;
    private Class<T> mClazz;
    private String TAG = "SQLiteDaoImpl";
    private static final Object[] mPutMethodArgs = new Object[2];
    private static final Map<String, Method> mPutMethods
            = new ArrayMap<>();

    @Override
    public void init(Class<T> clazz) {
        this.mClazz = clazz;
        Application application = Utils.getApp();
        File dbFile = application.getDatabasePath("dao.db");
        mSqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        StringBuffer sb = new StringBuffer();
        sb.append("create table if not exists ")
                .append(SQLiteDaoUtil.getTableName(mClazz))
                .append("(id integer primary key autoincrement, ");
        Field[] fields = mClazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);// 设置权限
            String name = field.getName();
            String type = field.getType().getSimpleName();
            sb.append(name).append(SQLiteDaoUtil.getColumnType(type)).append(", ");
        }
        sb.replace(sb.length() - 2, sb.length(), ")");
        String createTableSql = sb.toString();
        Log.e(TAG, "表语句--> " + createTableSql);
        mSqLiteDatabase.execSQL(createTableSql);
    }


    // 插入数据库 t 是任意对象
    @Override
    public long insert(T obj) {
        ContentValues values = contentValuesByObj(obj);
        return mSqLiteDatabase.insert(SQLiteDaoUtil.getTableName(mClazz),
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
    public List<T> query(T where) {
        ContentValues values = contentValuesByObj(where);
        SQLiteHandle whereHandle = new SQLiteHandle(where);
        Cursor cursor = mSqLiteDatabase.query(SQLiteDaoUtil.getTableName(mClazz), null, whereHandle.getWhereClause(),
                whereHandle.getWhereArgs(), null, null, null);
        return cursorToList(cursor);
    }


    @Override
    public int update(T data, T where) {
        ContentValues values = contentValuesByObj(data);
        SQLiteHandle whereHandle = new SQLiteHandle(where);
        return mSqLiteDatabase.update(SQLiteDaoUtil.getTableName(mClazz), values,
                whereHandle.getWhereClause(), whereHandle.getWhereArgs());
    }

    @Override
    public int delete(T where) {
        SQLiteHandle whereHandle = new SQLiteHandle(where);
        return mSqLiteDatabase.delete(SQLiteDaoUtil.getTableName(mClazz), whereHandle.getWhereClause(),
                whereHandle.getWhereArgs());
    }

    // obj 转成 ContentValues
    private ContentValues contentValuesByObj(T obj) {
        ContentValues values = new ContentValues();
        Field[] fields = mClazz.getDeclaredFields();
        for (Field field : fields) {
            try {
                // 设置权限，私有和共有都可以访问
                field.setAccessible(true);
                String key = field.getName();
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
        return mSqLiteDatabase.delete(SQLiteDaoUtil.getTableName(mClazz), whereClause, whereArgs);
    }

    /**
     * 更新  这些你需要对  最原始的写法比较明了 extends
     */
    public int update(T obj, String whereClause, String... whereArgs) {
        ContentValues values = contentValuesByObj(obj);
        return mSqLiteDatabase.update(SQLiteDaoUtil.getTableName(mClazz),
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
                        String name = field.getName();
                        // 获取角标
                        int index = cursor.getColumnIndex(name);
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
            typeName = SQLiteDaoUtil.capitalize(fieldType.getName());
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
