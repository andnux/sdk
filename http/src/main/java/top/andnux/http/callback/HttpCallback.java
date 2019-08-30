package top.andnux.http.callback;

import top.andnux.http.core.HttpResponse;

public interface HttpCallback {

    void onSuccess(HttpResponse response);

    void onFail(Exception e);

    void onComplete();
}
