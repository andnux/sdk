package top.andnux.sqlite;

import java.util.List;

public interface SQLiteDao<T> {

    // 插入数据
    long insert(T t);

    // 批量插入  检测性能
    void insert(List<T> datas);

    // 获取专门查询的支持类
    SQLiteQuery<T> querySupport();

    List<T> query(QueryWhere where);

    //删除
    int delete(String whereClause, String... whereArgs);

    int delete(QueryWhere where);

    //更新
    int update(T data, QueryWhere where);

    int update(T obj, String whereClause, String... whereArgs);
}
