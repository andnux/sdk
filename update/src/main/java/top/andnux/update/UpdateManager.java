package top.andnux.update;

import top.andnux.update.request.HttpRequest;

public class UpdateManager {

    private static final UpdateManager ourInstance = new UpdateManager();
    private HttpRequest mHttpClient;

    public static UpdateManager getInstance() {
        return ourInstance;
    }

    private UpdateManager() {

    }
}
