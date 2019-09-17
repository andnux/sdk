package top.andnux.sqlite.http;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;
import java.util.Map;

public interface SQLiteService {

    /**
     * 查询所有表名
     *
     * @param database
     * @return
     * @throws Exception
     */
    List<String> queryTables(SQLiteDatabase database) throws Exception;

    /**
     * 查询表名的所有列名
     *
     * @param database
     * @param tableName
     * @return
     * @throws Exception
     */
    List<String> queryColumnName(SQLiteDatabase database,
                                 String tableName) throws Exception;


    /**
     * 查询表数据
     *
     * @param database
     * @param tableName
     * @return
     * @throws Exception
     */
    List<Map<String,Object>> queryData(SQLiteDatabase database,
                                       String tableName) throws Exception;

    /**
     * 判断表名的列名是否存在
     *
     * @param database
     * @param tableName
     * @param columnName
     * @return
     * @throws Exception
     */
    boolean queryColumnExits(SQLiteDatabase database,
                             String tableName,
                             String columnName) throws Exception;
}
