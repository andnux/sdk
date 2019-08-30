package top.andnux.http.cookie;

public interface CookieStorage {

    void put(String url, String cookie);

    String get(String url);
}
