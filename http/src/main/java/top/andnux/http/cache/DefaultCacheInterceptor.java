package top.andnux.http.cache;

public class DefaultCacheInterceptor implements CacheInterceptor {
    @Override
    public String interceptor(String data) {
        return data;
    }
}
