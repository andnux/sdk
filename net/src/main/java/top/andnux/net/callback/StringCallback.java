package top.andnux.net.callback;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;

import top.andnux.net.HttpManager;
import top.andnux.net.cache.Cache;
import top.andnux.net.core.HttpConfig;
import top.andnux.net.core.HttpResponse;
import top.andnux.net.utils.Utils;

public abstract class StringCallback implements HttpCallback {

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public abstract void onSuccess(String string);

    @Override
    public void onSuccess(HttpResponse response) {
        try {
            Log.e("cache", response.isCache() + "");
            final String s = Utils.inputStream2String(response.getInputStream());
            HttpConfig httpConfig = HttpManager.getInstance().getHttpConfig();
            Cache cache = httpConfig.getCache();
            if (cache != null) {
                cache.put(Utils.md5(response.getRequest().getUrl()),
                        s, httpConfig.getCacheDuration());
            }
            mHandler.post(() -> onSuccess(s));
        } catch (IOException e) {
            e.printStackTrace();
            onFail(e);
        }
    }
}
