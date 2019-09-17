package top.andnux.sqlite.http;

import fi.iki.elonen.NanoHTTPD;

public interface HttpHandler {

    boolean canHandler(String url);

    NanoHTTPD.Response handler(NanoHTTPD.IHTTPSession session);
}
