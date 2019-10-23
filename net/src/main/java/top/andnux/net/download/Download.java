package top.andnux.net.download;

public interface Download {

    long getContentLength(String url);

    long download(String url);
}
