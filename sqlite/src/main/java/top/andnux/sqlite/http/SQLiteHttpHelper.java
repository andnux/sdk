package top.andnux.sqlite.http;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.List;
import java.util.Map;

public class SQLiteHttpHelper {

    private static final SQLiteHttpHelper ourInstance = new SQLiteHttpHelper();
    private SQLiteDatabase mDatabase;
    private SQLiteService mSQLiteProvider = new SQLiteServiceImpl();

    public static SQLiteHttpHelper getInstance() {
        return ourInstance;
    }

    private SQLiteHttpHelper() {

    }

    public void start(Context context) {
        Intent intent = new Intent(context, HttpService.class);
        context.startService(intent);
    }

    public void stop(Context context) {
        Intent intent = new Intent(context, HttpService.class);
        context.stopService(intent);
    }

    public SQLiteService getSQLiteProvider() {
        return mSQLiteProvider;
    }

    public void setSQLiteProvider(SQLiteService SQLiteProvider) {
        mSQLiteProvider = SQLiteProvider;
    }

    public static void init(Application application, boolean debug, String dbPath) {
        SQLiteHttpHelper instance = getInstance();
        instance.mDatabase = SQLiteDatabase.openOrCreateDatabase(dbPath, null);
        if (debug) {
            instance.start(application);
        }
    }

    public static void init(Application application, boolean debug, File dbFile) {
        SQLiteHttpHelper instance = getInstance();
        instance.mDatabase = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        if (debug) {
            instance.start(application);
        }
    }

    public List<String> queryTables() throws Exception {
        if (mSQLiteProvider == null) mSQLiteProvider = new SQLiteServiceImpl();
        return mSQLiteProvider.queryTables(mDatabase);
    }

    public List<String> queryColumnName(String tableName) throws Exception {
        if (mSQLiteProvider == null) mSQLiteProvider = new SQLiteServiceImpl();
        return mSQLiteProvider.queryColumnName(mDatabase, tableName);
    }

    public List<Map<String, Object>> queryData(String tableName) throws Exception {
        if (mSQLiteProvider == null) mSQLiteProvider = new SQLiteServiceImpl();
        return mSQLiteProvider.queryData(mDatabase, tableName);
    }

    public boolean queryColumnExits(String tableName, String columnName) throws Exception {
        if (mSQLiteProvider == null) mSQLiteProvider = new SQLiteServiceImpl();
        return mSQLiteProvider.queryColumnExits(mDatabase, tableName, columnName);
    }
}
