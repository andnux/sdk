package top.andnux.utils.storage;

public interface StorageDao<T> {

    //必须在使用之前才调用有效(有可能文件名是动态的)
    void changeFileName(String fileName);

    //同步保存
    void save(T data) throws Exception;

    //异步保存
    void save(T data, StorageListener<T> listener);

    //同步读取
    T load() throws Exception;

    //异步读取
    void load(StorageListener<T> listener);

    //清除数据
    void clear();

    //关闭线程池
    void shutdown();
}
