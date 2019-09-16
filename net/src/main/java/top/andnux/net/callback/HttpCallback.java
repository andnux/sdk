package top.andnux.net.callback;

import top.andnux.net.core.HttpResponse;

public interface HttpCallback {

    void onSuccess(HttpResponse response);

    void onFail(Exception e);

    void onComplete();
}
