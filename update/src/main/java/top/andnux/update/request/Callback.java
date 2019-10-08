package top.andnux.update.request;

public interface Callback<T> {

    void onSuccess(T data);

    void onFail(Exception e);
}
