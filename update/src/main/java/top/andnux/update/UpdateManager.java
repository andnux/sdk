package top.andnux.update;

import top.andnux.update.installer.ApkInstaller;
import top.andnux.update.installer.Installer;
import top.andnux.update.request.HttpClient;

public class UpdateManager {

    private HttpClient mHttpClient = new HttpClient();
    private Installer mInstaller = new ApkInstaller();

    public static UpdateManager getInstance() {
        return new UpdateManager();
    }

    public UpdateManager setInstaller(Installer installer) {
        mInstaller = installer;
        return this;
    }

    public UpdateManager setHttpClient(HttpClient httpClient) {
        mHttpClient = httpClient;
        return this;
    }

    private UpdateManager() {

    }

    /*
    开始更新检查
     */
    public void update() {

    }
}
