package top.andnux.update.request;

import android.util.ArrayMap;

import java.io.InputStream;
import java.util.Map;

public class HttpClient {

    private String mUrl;
    private Http mHttp = new DefaultHttp();
    private Map<String, String> mHeaders = new ArrayMap<>();
    private Map<String, Object> mParams = new ArrayMap<>();
    private Callback<InputStream> mCallback;

    public static HttpClient newInstance() {
        return new HttpClient();
    }

    public HttpClient setUrl(String url) {
        mUrl = url;
        return this;
    }

    public HttpClient setHttp(Http http) {
        mHttp = http;
        return this;
    }

    public HttpClient setCallback(Callback<InputStream> callback) {
        mCallback = callback;
        return this;
    }

    public HttpClient addHeader(String key, String value) {
        mHeaders.put(key, value);
        return this;
    }

    public HttpClient addHeaders(Map<String, String> value) {
        mHeaders.putAll(value);
        return this;
    }

    public HttpClient addParams(String key, Object value) {
        mParams.put(key, value);
        return this;
    }

    public HttpClient addParams(Map<String, String> value) {
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
