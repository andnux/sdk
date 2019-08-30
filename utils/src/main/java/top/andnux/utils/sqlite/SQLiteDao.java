package top.andnux.utils.sqlite;

import java.util.List;

public interface SQLiteDao<T> {

    void init(Class<T> clazz);

    // 插入数据
    long insert(T t);

    // 批量插入  检测性能
    void insert(List<T> datas);

    // 获取专门查询的支持类
    SQLiteQuery<T> querySupport();

    List<T> query(T where);

    int update(T data, T where);

    int delete(T where);

    // 按照语句查询
    int delete(String whereClause, String... whereArgs);

    int update(T obj, String whereClause, String... whereArgs);
}
