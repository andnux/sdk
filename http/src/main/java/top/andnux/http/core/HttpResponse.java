package top.andnux.http.core;

import java.io.InputStream;
import java.util.Map;

public class HttpResponse {

    private int mCode;
    private int mContentLength;
    private InputStream mInputStream;
    private Map<String, String> mHeader;
    private boolean mCache;
    private HttpRequest mRequest;

    public boolean isCache() {
        return mCache;
    }

    public void setCache(boolean cache) {
        this.mCache = cache;
    }

    public HttpRequest getRequest() {
        return mRequest;
    }

    public void setRequest(HttpRequest request) {
        mRequest = request;
    }

    public int getContentLength() {
        return mContentLength;
    }

    public void setContentLength(int contentLength) {
        mContentLength = contentLength;
    }

    public InputStream getInputStream() {
        return mInputStream;
    }

    public void setInputStream(InputStream inputStream) {
        mInputStream = inputStream;
    }

    public Map<String, String> getHeader() {
        return mHeader;
    }

    public void setHeader(Map<String, String> header) {
        mHeader = header;
    }

    public int getCode() {
        return mCode;
    }

    public void setCode(int code) {
        mCode = code;
    }
}
