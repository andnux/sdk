package top.andnux.update.request;

import java.io.InputStream;
import java.util.Map;

public class DefaultHttp implements Http {

    @Override
    public InputStream get(String url,
                           Map<String, String> header,
                           Map<String, Object> params,
                           Callback<InputStream> callback) {
        return null;
    }

    @Override
    public InputStream post(String url,
                            Map<String, String> header,
                            Map<String, Object> params,
                            Callback<InputStream> callback) {
        return null;
    }
}
