package top.andnux.sqlite.http;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fi.iki.elonen.NanoHTTPD;

public class MyNanoHTTPD extends NanoHTTPD {

    private List<HttpHandler> mHandlers = new ArrayList<>();

    MyNanoHTTPD(Context context) throws Exception {
        super(8080);
        mHandlers.add(new TableHandler());
        mHandlers.add(new DataHandler());
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        Log.e("TAG", "\nRunning! http://" + getLocalIpStr(context) + ":8080 /\n");
    }

    //获取IP地址
    private String getLocalIpStr(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = null;
        if (wifiManager != null) {
            wifiInfo = wifiManager.getConnectionInfo();
            return intToIpAddr(wifiInfo.getIpAddress());
        }
        return "";
    }

    private String intToIpAddr(int ip) {
        return (ip & 0xFF) + "."
                + ((ip >> 8) & 0xFF) + "."
                + ((ip >> 16) & 0xFF) + "."
                + ((ip >> 24) & 0xFF);
    }

    @Override
    public Response serve(IHTTPSession session) {
        HttpHandler httpHandler = null;
        String uri = session.getUri();
        for (HttpHandler handler : mHandlers) {
            if (handler.canHandler(uri)) {
                httpHandler = handler;
                break;
            }
        }
        if (httpHandler != null) {
            return httpHandler.handler(session);
        }
        StringBuilder msg = new StringBuilder("<html><body>\n");
        msg.append("<p>").append(session.getUri()).append("</p>\n");
        Map<String, String> parms = session.getParms();
        Set<Map.Entry<String, String>> entries = parms.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            msg.append("<p>").append(entry.getKey()).append(":")
                    .append(entry.getValue()).append("</p>\n");
        }
        return NanoHTTPD.newFixedLengthResponse(msg + "</body></html>\n");
    }
}
