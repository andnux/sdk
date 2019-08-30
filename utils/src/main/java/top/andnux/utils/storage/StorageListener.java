package top.andnux.utils.storage;

public interface StorageListener<T> {

    void onSuccess(T data);

    void onFail(Exception e);

    void onComplete();
}
