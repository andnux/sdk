package top.andnux.net.download;

public class DownloadManager {

    private static final DownloadManager ourInstance = new DownloadManager();

    public static DownloadManager getInstance() {
        return ourInstance;
    }

    private DownloadManager() {

    }
}
