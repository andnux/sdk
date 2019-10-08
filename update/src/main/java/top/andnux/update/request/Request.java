package top.andnux.update.request;

import java.io.InputStream;
import java.util.Map;

public interface Request {

    InputStream get(String url, Map<String, String> header, Map<String, Object> params, Callback<InputStream> callback);

    InputStream post(String url, Map<String, String> header, Map<String, Object> params, Callback<InputStream> callback);

}
