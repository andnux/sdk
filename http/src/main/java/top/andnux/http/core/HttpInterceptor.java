package top.andnux.http.core;

import java.io.IOException;

public interface HttpInterceptor {

    void intercept(HttpConfig config) throws IOException;

    void intercept(HttpRequest request) throws IOException;
}
