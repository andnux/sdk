package top.andnux.net;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import top.andnux.net.core.HttpConfig;
import top.andnux.net.core.HttpRequest;
import top.andnux.net.retrofit.adapter.JsonConverterFactory;
import top.andnux.net.retrofit.converter.LiveDataCallAdapterFactory;

public class HttpManager {

    private static final HttpManager ourInstance = new HttpManager();

    public static HttpManager getInstance() {
        return ourInstance;
    }

    private HttpConfig mHttpConfig = new HttpConfig();
    private Retrofit mRetrofit;
    private OkHttpClient mClient;
    private ExecutorService mService;
    private List<CallAdapter.Factory> mCallAdapterFactorie = new ArrayList<>();
    private List<Interceptor> mInterceptors = new ArrayList<>();
    private List<Converter.Factory> mConverterFactory = new ArrayList<>();

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

    public void addCallAdapterFactory(CallAdapter.Factory factory) {
        mCallAdapterFactorie.add(factory);
    }

    public void addInterceptor(Interceptor interceptor) {
        mInterceptors.add(interceptor);
    }

    public void addCallAdapterFactory(Converter.Factory factory) {
        mConverterFactory.add(factory);
    }

    private void initRetrofit() {
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(logInterceptor)
                .readTimeout(mHttpConfig.getReadTimeout(), TimeUnit.SECONDS)
                .writeTimeout(mHttpConfig.getWriteTimeout(), TimeUnit.SECONDS)
                .connectTimeout(mHttpConfig.getConnectTimeout(), TimeUnit.SECONDS);
        for (Interceptor interceptor : mInterceptors) {
            builder.addInterceptor(interceptor);
        }
        mClient = builder.build();
        Retrofit.Builder mRetrofitBuilder = new Retrofit.Builder()
                .client(mClient)
                .baseUrl(mHttpConfig.getHost())
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(JsonConverterFactory.create())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addCallAdapterFactory(LiveDataCallAdapterFactory.create());
        for (CallAdapter.Factory factory : mCallAdapterFactorie) {
            mRetrofitBuilder.addCallAdapterFactory(factory);
        }
        for (Converter.Factory factory : mConverterFactory) {
            mRetrofitBuilder.addConverterFactory(factory);
        }
        mRetrofit = mRetrofitBuilder.build();
    }

    public <T> T create(Class<T> service) {
        if (mRetrofit == null) {
            initRetrofit();
        }
        return mRetrofit.create(service);
    }
}
