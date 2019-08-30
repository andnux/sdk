package top.andnux.http;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import top.andnux.http.core.HttpConfig;
import top.andnux.http.core.HttpRequest;

public class HttpManager {

    private static final HttpManager ourInstance = new HttpManager();

    public static HttpManager getInstance() {
        return ourInstance;
    }

    private HttpConfig mHttpConfig = new HttpConfig();
    private Retrofit mRetrofit;
    private OkHttpClient mClient;
    private ExecutorService mService;

    private HttpManager() {
        mService = Executors.newCachedThreadPool();
    }

    public HttpConfig getHttpConfig() {
        return mHttpConfig;
    }

    public void setHttpConfig(HttpConfig httpConfig) {
        mHttpConfig = httpConfig;
    }

    public void sendRequest(final HttpRequest request) {
        mService.submit(() -> {
            if (request.getHttpEngine() != null) {
                request.getHttpEngine().execute(request);
            } else {
                mHttpConfig.getHttpEngine().execute(request);
            }
        });
    }

    public HttpRequest newHttpRequest() {
        return new HttpRequest();
    }

    private void initRetrofit() {
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        mClient = new OkHttpClient.Builder()
                .addInterceptor(logInterceptor)
                .readTimeout(mHttpConfig.getReadTimeout(), TimeUnit.SECONDS)
                .writeTimeout(mHttpConfig.getWriteTimeout(), TimeUnit.SECONDS)
                .connectTimeout(mHttpConfig.getConnectTimeout(), TimeUnit.SECONDS)
                .build();
        mRetrofit = new Retrofit.Builder()
                .client(mClient)
                .baseUrl(mHttpConfig.getHost())
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public <T> T create(Class<T> service) {
        if (mRetrofit == null) {
            initRetrofit();
        }
        return mRetrofit.create(service);
    }
}
