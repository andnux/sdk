package top.andnux.sqlite;

import android.database.sqlite.SQLiteDatabase;

import java.io.File;

public class SQLiteManager {

    private static final SQLiteManager ourInstance = new SQLiteManager();
    public static SQLiteManager getInstance() {
        return ourInstance;
    }
    private SQLiteDatabase mDatabase;
    private SQLiteDaoImpl<Object> mDao;

    private SQLiteManager() {
    }

    public static void init(File dbFile) {
        getInstance().mDatabase = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
    }

    @SuppressWarnings("all")
    public <T> SQLiteDao<T> getDao(Class<T> clazz) {
        if (mDao == null) {
            mDao = (SQLiteDaoImpl<Object>) new SQLiteDaoImpl<T>(mDatabase, clazz);
        }
        return (SQLiteDao<T>) mDao;
    }
}
