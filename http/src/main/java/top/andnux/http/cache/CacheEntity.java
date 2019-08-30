package top.andnux.http.cache;

public class CacheEntity {

    private String url;
    private String data;
    private Long time;
    private Long duration;

    public CacheEntity() {
    }

    public CacheEntity(String url, String data, long time, long duration) {
        this.url = url;
        this.data = data;
        this.time = time;
        this.duration = duration;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "CacheEntity{" +
                "url='" + url + '\'' +
                ", data='" + data + '\'' +
                ", time=" + time +
                ", duration=" + duration +
                '}';
    }
}
