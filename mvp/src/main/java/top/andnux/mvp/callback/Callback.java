package top.andnux.mvp.callback;

public interface Callback<T> {

    void onSuccess(T data);

    void onFailed(Throwable e);

    void onComplete();
}
