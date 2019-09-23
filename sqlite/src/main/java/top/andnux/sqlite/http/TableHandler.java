package top.andnux.sqlite.http;

import android.text.TextUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

import fi.iki.elonen.NanoHTTPD;

public class TableHandler implements HttpHandler {
    @Override
    public boolean canHandler(String url) {
        return url.contains("table");
    }

    @Override
    public NanoHTTPD.Response handler(NanoHTTPD.IHTTPSession session) {
        String table = session.getParms().get("name");
        SQLiteHttpHelper instance = SQLiteHttpHelper.getInstance();
        try {
            if (TextUtils.isEmpty(table)) {
                List<String> list = instance.queryTables();
                return ResponseHelper.success(list);
            } else{
                List<String> list = instance.queryColumnName(table);
                return ResponseHelper.success(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuilder msg = new StringBuilder("<html><body>\n");
        msg.append("<p>").append(session.getUri()).append("</p>\n");
        Map<String, String> parms = session.getParms();
        Set<Map.Entry<String, String>> entries = parms.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            msg.append("<p>").append(entry.getKey()).append(":")
                    .append(entry.getValue()).append("</p>\n");
        }
        return ResponseHelper.success(msg);
    }
}
