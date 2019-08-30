package top.andnux.http.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import top.andnux.http.cache.Cache;
import top.andnux.http.cache.CacheInterceptor;
import top.andnux.http.cache.DefaultCacheInterceptor;
import top.andnux.http.cache.DoubleCache;
import top.andnux.http.cookie.CookieStorage;
import top.andnux.http.cookie.DoubleCookieStorage;

public class HttpConfig {

    private String mHost;
    private long mConnectTimeout = 30 * 1000;
    private long mReadTimeout = 30 * 1000;
    private long mWriteTimeout = 30 * 1000;
    private Cache mCache = new DoubleCache();
    private CookieStorage mCookieStorage = new DoubleCookieStorage();
    private int mRetryCount = 3;
    private int mCacheDuration = 0;
    private CacheInterceptor mCacheInterceptor = new DefaultCacheInterceptor();
    private List<HttpInterceptor> mHttpInterceptors = new ArrayList<>();
    private Map<String, String> mHeader = new HashMap<>();
    private HttpEngine mHttpEngine = new DefaultHttpEngine();
    private Map<String, Object> mParameter = new HashMap<>();

    public CacheInterceptor getCacheInterceptor() {
        return mCacheInterceptor;
    }

    public void setCacheInterceptor(CacheInterceptor cacheInterceptor) {
        mCacheInterceptor = cacheInterceptor;
    }

    public Map<String, Object> getParameter() {
        return mParameter;
    }

    public void setParameter(Map<String, Object> parameter) {
        mParameter = parameter;
    }

    public HttpEngine getHttpEngine() {
        return mHttpEngine;
    }

    public void setHttpEngine(HttpEngine httpEngine) {
        mHttpEngine = httpEngine;
    }

    public Map<String, String> getHeader() {
        return mHeader;
    }

    public void setHeader(Map<String, String> header) {
        mHeader = header;
    }

    public List<HttpInterceptor> getHttpInterceptors() {
        return mHttpInterceptors;
    }

    public void setHttpInterceptors(List<HttpInterceptor> httpInterceptors) {
        mHttpInterceptors = httpInterceptors;
    }

    public String getHost() {
        return mHost;
    }

    public void setHost(String host) {
        mHost = host;
    }

    public long getConnectTimeout() {
        return mConnectTimeout;
    }

    public void setConnectTimeout(long connectTimeout) {
        mConnectTimeout = connectTimeout;
    }

    public long getReadTimeout() {
        return mReadTimeout;
    }

    public void setReadTimeout(long readTimeout) {
        mReadTimeout = readTimeout;
    }

    public long getWriteTimeout() {
        return mWriteTimeout;
    }

    public void setWriteTimeout(long writeTimeout) {
        mWriteTimeout = writeTimeout;
    }

    public Cache getCache() {
        return mCache;
    }

    public void setCache(Cache cache) {
        mCache = cache;
    }

    public CookieStorage getCookieStorage() {
        return mCookieStorage;
    }

    public void setCookieStorage(CookieStorage cookieStorage) {
        mCookieStorage = cookieStorage;
    }

    public int getRetryCount() {
        return mRetryCount;
    }

    public void setRetryCount(int retryCount) {
        mRetryCount = retryCount;
    }

    public int getCacheDuration() {
        return mCacheDuration;
    }

    public void setCacheDuration(int cacheDuration) {
        mCacheDuration = cacheDuration;
    }
}
