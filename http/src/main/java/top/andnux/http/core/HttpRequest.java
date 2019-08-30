package top.andnux.http.core;

import java.util.HashMap;
import java.util.Map;

import top.andnux.http.callback.HttpCallback;

public class HttpRequest {

    private String mUrl;
    private Map<String, String> mHeader = new HashMap<>();
    private Map<String, Object> mParameter = new HashMap<>();
    private BodyType mBodyType = BodyType.FORM;
    private HttpMethod mHttpMethod = HttpMethod.GET;
    private HttpCallback mHttpCallback;
    private int mCacheDuration = 0;
    private HttpEngine mHttpEngine = new DefaultHttpEngine();

    public HttpEngine getHttpEngine() {
        return mHttpEngine;
    }

    public void setHttpEngine(HttpEngine httpEngine) {
        mHttpEngine = httpEngine;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public Map<String, String> getHeader() {
        return mHeader;
    }

    public void setHeader(Map<String, String> header) {
        mHeader = header;
    }

    public Map<String, Object> getParameter() {
        return mParameter;
    }

    public void setParameter(Map<String, Object> parameter) {
        mParameter = parameter;
    }

    public BodyType getBodyType() {
        return mBodyType;
    }

    public void setBodyType(BodyType bodyType) {
        mBodyType = bodyType;
    }

    public HttpMethod getHttpMethod() {
        return mHttpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        mHttpMethod = httpMethod;
    }

    public HttpCallback getHttpCallback() {
        return mHttpCallback;
    }

    public void setHttpCallback(HttpCallback httpCallback) {
        mHttpCallback = httpCallback;
    }

    public int getCacheDuration() {
        return mCacheDuration;
    }

    public void setCacheDuration(int cacheDuration) {
        mCacheDuration = cacheDuration;
    }
}
