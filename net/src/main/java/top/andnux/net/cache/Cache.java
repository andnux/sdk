package top.andnux.net.cache;

public interface Cache {

    void put(String url, String value, long time);

    String get(String url);

    void remove(String url);

    void clear();
}
