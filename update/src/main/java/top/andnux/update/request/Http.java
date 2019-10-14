package top.andnux.update.request;

import java.io.InputStream;
import java.util.Map;

public interface Http {

    /**
     * GET 请求
     *
     * @param url
     * @param header
     * @param params
     * @param callback
     * @return
     */
    InputStream get(String url, Map<String,
            String> header,
                    Map<String, Object> params,
                    Callback<InputStream> callback);

    /**
     * POST 请求
     *
     * @param url
     * @param header
     * @param params
     * @param callback
     * @return
     */
    InputStream post(String url,
                     Map<String, String> header,
                     Map<String, Object> params,
                     Callback<InputStream> callback);
}
