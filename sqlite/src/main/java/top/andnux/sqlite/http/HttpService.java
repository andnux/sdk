package top.andnux.sqlite.http;

import android.app.IntentService;
import android.content.Intent;

public class HttpService extends IntentService {

    public HttpService() {
        super("HttpService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            new MyNanoHTTPD(this);
        } catch (Exception ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
    }
}
