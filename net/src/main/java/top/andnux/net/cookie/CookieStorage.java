package top.andnux.net.cookie;

public interface CookieStorage {

    void put(String url, String cookie);

    String get(String url);
}
