package top.andnux.net.cache;

public class DefaultCacheInterceptor implements CacheInterceptor {
    @Override
    public String interceptor(String data) {
        return data;
    }
}
