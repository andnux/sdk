package top.andnux.update.request;

import android.util.ArrayMap;

import java.io.InputStream;
import java.util.Map;

public class HttpRequest {

    private Request mHttp;
    private String mUrl;
    private Map<String, String> mHeaders = new ArrayMap<>();
    private Map<String, Object> mParams = new ArrayMap<>();
    private Callback<InputStream> mCallback;

    public static HttpRequest newInstance() {
        return new HttpRequest();
    }

    public HttpRequest setUrl(String url) {
        mUrl = url;
        return this;
    }

    public HttpRequest setHttp(Request http) {
        mHttp = http;
        return this;
    }

    public HttpRequest setCallback(Callback<InputStream> callback) {
        mCallback = callback;
        return this;
    }

    public HttpRequest addHeader(String key, String value) {
        mHeaders.put(key, value);
        return this;
    }

    public HttpRequest addHeaders(Map<String, String> value) {
        mHeaders.putAll(value);
        return this;
    }

    public HttpRequest addParams(String key, Object value) {
        mParams.put(key, value);
        return this;
    }

    public HttpRequest addParams(Map<String, String> value) {
        mParams.putAll(value);
        return this;
    }

    public InputStream get() {
        return mHttp.get(mUrl, mHeaders, mParams, mCallback);
    }

    public InputStream post() {
        return mHttp.post(mUrl, mHeaders, mParams, mCallback);
    }
}
