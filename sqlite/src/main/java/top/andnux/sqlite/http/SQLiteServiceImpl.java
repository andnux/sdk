package top.andnux.sqlite.http;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLiteServiceImpl implements SQLiteService {

    private static final String TAG = "SQLiteServiceImpl";

    @Override
    public List<String> queryTables(SQLiteDatabase database) throws Exception {
        String sql = "select name from sqlite_master where type='table' order by name";
        Cursor cursor = null;
        List<String> list = new ArrayList<>();
        try {
            cursor = database.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                String name = cursor.getString(0);
                list.add(name);
            }
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return list;
    }

    @Override
    public List<String> queryColumnName(SQLiteDatabase database,
                                        String tableName) throws Exception {
        String sql = "select * from " + tableName;
        Cursor cursor = null;
        List<String> list = new ArrayList<>();
        try {
            cursor = database.rawQuery(sql, null);
            String[] columnNames = cursor.getColumnNames();
            list.addAll(Arrays.asList(columnNames));
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return list;
    }

    private Object getObject(Cursor cursor, String name) {
        int type = cursor.getType(cursor.getColumnIndex(name));
        return cursor.getString(cursor.getColumnIndex(name));
    }

    @Override
    public List<Map<String, Object>> queryData(SQLiteDatabase database,
                                               String tableName) throws Exception {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "select * from " + tableName;
        Cursor cursor = null;
        try {
            cursor = database.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                Map<String, Object> map = new HashMap<>();
                String[] columnNames = cursor.getColumnNames();
                for (String name : columnNames) {
                    map.put(name, getObject(cursor, name));
                }
                list.add(map);
            }

        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return list;
    }

    @Override
    public boolean queryColumnExits(SQLiteDatabase database,
                                    String tableName,
                                    String columnName) throws Exception {
        boolean result = false;
        Cursor cursor = null;
        try {
            cursor = database.rawQuery("select * from sqlite_master where name = ? and sql like ?"
                    , new String[]{tableName, "%" + columnName + "%"});
            result = null != cursor && cursor.moveToFirst();
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return result;
    }
}
