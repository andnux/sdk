package top.andnux.utils.sqlite;

public class SQLiteDaoFactory {

    private static SQLiteDao<Object> SQ_LITE_DAO = null;

    public static <T> SQLiteDao<T> getDao(Class<T> clazz) {
        if (SQ_LITE_DAO == null) {
            SQ_LITE_DAO = (SQLiteDao<Object>) new SQLiteDaoImpl<T>();
            SQ_LITE_DAO.init((Class<Object>) clazz);
        }
        return (SQLiteDao<T>) SQ_LITE_DAO;
    }
}
