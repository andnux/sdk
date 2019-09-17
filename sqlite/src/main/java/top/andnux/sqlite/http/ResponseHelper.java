package top.andnux.sqlite.http;

import fi.iki.elonen.NanoHTTPD;
import top.andnux.json.JsonAdapterManager;

public class ResponseHelper {

    public static <T> NanoHTTPD.Response success(T data) {
        ResponseBean<T> responseBean = new ResponseBean<>();
        responseBean.setData(data);
        responseBean.setCode(200);
        responseBean.setMsg("成功");
        NanoHTTPD.Response response = NanoHTTPD.newFixedLengthResponse(
                NanoHTTPD.Response.Status.OK, "text/json",
                JsonAdapterManager.getInstance().toJSONString(responseBean));
        response.addHeader("Content-Type", "application/json;charset=UTF-8");
        return response;
    }
}
