package top.andnux.http.core;

import android.util.Log;

import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import top.andnux.http.HttpManager;
import top.andnux.http.cache.Cache;
import top.andnux.http.callback.HttpCallback;
import top.andnux.http.cookie.CookieStorage;
import top.andnux.http.utils.Utils;

/**
 * 简单的http请求，不支持文件上传
 * 自动处理cookie
 * 支持 POST，GET
 */
public class DefaultHttpEngine implements HttpEngine {

    private static final String KEY_HEADER_COOKIE = "Set-Cookie";
    private Gson mGson = new Gson();

    protected byte[] makeBody(HttpConfig config, HttpRequest request) {
        if (request.getHttpMethod() == HttpMethod.POST) {
            if (request.getBodyType() == BodyType.FORM) {
                return addFormBody(config, request);
            } else {
                Map<String, Object> parameter = config.getParameter();
                parameter.putAll(request.getParameter());
                String s = mGson.toJson(parameter);
                return s.getBytes(StandardCharsets.UTF_8);
            }
        }
        return null;
    }

    @Override
    public void execute(HttpRequest request) {
        try {
            HttpConfig config = HttpManager.getInstance().getHttpConfig();
            List<HttpInterceptor> httpInterceptors = config.getHttpInterceptors();
            for (HttpInterceptor interceptor : httpInterceptors) {
                interceptor.intercept(config);
                interceptor.intercept(request);
            }
            String fUrl = request.getUrl();
            if (request.getHttpMethod() == HttpMethod.GET) {
                fUrl = appendUrl(config, request);
            }
            request.setUrl(fUrl);
            Cache cache = config.getCache();
            if (cache != null) {
                String s = config.getCacheInterceptor().interceptor(cache.get(Utils.md5(fUrl)));
                if (s != null && !s.equals("")) {
                    HttpCallback httpCallback = request.getHttpCallback();
                    byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
                    HttpResponse response = new HttpResponse();
                    response.setRequest(request);
                    response.setInputStream(new ByteArrayInputStream(bytes));
                    response.setContentLength(bytes.length);
                    response.setCache(true);
                    response.setHeader(new HashMap<>());
                    if (httpCallback != null) {
                        httpCallback.onSuccess(response);
                    }
                    return;
                }
            }
            Log.e("http", "地址：" + request.getUrl());
            URL url = new URL(fUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (request.getHttpMethod() == HttpMethod.GET) {
                conn.setRequestMethod("GET");// 提交模式
            } else if (request.getHttpMethod() == HttpMethod.POST) {
                conn.setRequestMethod("POST");// 提交模式
            }
            conn.setReadTimeout((int) config.getReadTimeout());
            conn.setConnectTimeout((int) config.getConnectTimeout());
            conn.setDoInput(true);
            conn.setDoOutput(true);
            addHeader(config, request, conn);
            CookieStorage storage = config.getCookieStorage();
            String cookie = storage.get(url.getHost());
            System.out.println("cookie = " + cookie);
            conn.setRequestProperty(KEY_HEADER_COOKIE, cookie);
            OutputStream outputStream = conn.getOutputStream();
            byte[] body = makeBody(config, request);
            if (body != null) {
                outputStream.write(body);
            }
            outputStream.flush();
            outputStream.close();
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                HttpCallback httpCallback = request.getHttpCallback();
                HttpResponse response = new HttpResponse();
                response.setRequest(request);
                response.setCode(responseCode);
                response.setInputStream(conn.getInputStream());
                response.setContentLength(conn.getContentLength());
                Map<String, List<String>> headerFields = conn.getHeaderFields();
                Set<Map.Entry<String, List<String>>> entries = headerFields.entrySet();
                Map<String, String> header = new HashMap<>();
                for (Map.Entry<String, List<String>> entry : entries) {
                    header.put(entry.getKey(), entry.getValue().toString());
                    if (KEY_HEADER_COOKIE.equals(entry.getKey())) {
                        storage.put(url.getHost(), entry.getValue().toString());
                    }
                }
                response.setCache(false);
                if (httpCallback != null) {
                    httpCallback.onSuccess(response);
                }
            } else {
                HttpCallback httpCallback = request.getHttpCallback();
                String s = Utils.inputStream2String(conn.getErrorStream());
                if (httpCallback != null) {
                    httpCallback.onFail(new RuntimeException(s));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpCallback httpCallback = request.getHttpCallback();
        if (httpCallback != null) {
            httpCallback.onComplete();
        }
    }


    private String appendUrl(HttpConfig config, HttpRequest request) {
        StringBuilder url = new StringBuilder(request.getUrl());
        Map<String, Object> parameter = config.getParameter();
        parameter.putAll(request.getParameter());
        Set<String> strings = parameter.keySet();
        if (!url.toString().contains("?")) {
            url.append("?");
        }
        for (String string : strings) {
            url.append(string);
            url.append("=");
            Object obj = parameter.get(string);
            url.append(obj);
            url.append("&");
        }
        String endUrl = url.toString();
        if (url.toString().endsWith("&")) {
            endUrl = url.substring(0, url.length() - 1);
        }
        if (url.toString().endsWith("?")) {
            endUrl = url.substring(0, url.length() - 1);
        }
        return endUrl;
    }

    private byte[] addFormBody(HttpConfig config, HttpRequest request) {
        StringBuilder body = new StringBuilder();
        Map<String, Object> parameter = config.getParameter();
        parameter.putAll(request.getParameter());
        Set<String> strings = parameter.keySet();
        for (String string : strings) {
            body.append(string);
            body.append("=");
            Object obj = parameter.get(string);
            body.append(obj);
            body.append("&");
        }
        String endBody = body.toString();
        if (endBody.endsWith("&")) {
            endBody = body.substring(0, endBody.length() - 1);
        }
        return endBody.getBytes(StandardCharsets.UTF_8);
    }

    private void addHeader(HttpConfig config, HttpRequest request, HttpURLConnection conn) {
        Map<String, String> header = config.getHeader();
        if (header == null) {
            header = new HashMap<>();
        }
        header.putAll(request.getHeader());
        Set<String> keySet = header.keySet();
        for (String key : keySet) {
            conn.setRequestProperty(key, header.get(key));
        }
    }
}
