package top.andnux.http.cookie;

public class DoubleCookieStorage implements CookieStorage {

    private CookieStorage mMemCookieStorage = new MemoryCookieStorage();
    private CookieStorage mSpCookieStorage = new XmlCookieStorage();

    @Override
    public void put(String url, String cookie) {
        mMemCookieStorage.put(url, cookie);
        mSpCookieStorage.put(url, cookie);
    }

    @Override
    public String get(String url) {
        String s = mMemCookieStorage.get(url);
        if (s == null || "".equals(s)) {
            s = mSpCookieStorage.get(url);
        }
        return s;
    }
}
