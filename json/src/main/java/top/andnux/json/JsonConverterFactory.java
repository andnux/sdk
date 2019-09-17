package top.andnux.json;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public final class JsonConverterFactory extends Converter.Factory {

    private JsonAdapter mJsonAdapter;

    public JsonConverterFactory(JsonAdapter jsonAdapter) {
        mJsonAdapter = jsonAdapter;
    }

    public static JsonConverterFactory create(JsonAdapter converter) {
        return new JsonConverterFactory(converter);
    }


    public static JsonConverterFactory create() {
        return new JsonConverterFactory(new FastJsonAdapter());
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type,
                                                            Annotation[] annotations,
                                                            Retrofit retrofit) {
        return new JsonResponseBodyConverter<>(mJsonAdapter,type);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations,
                                                          Annotation[] methodAnnotations,
                                                          Retrofit retrofit) {
        return new JsonResponseBodyConverter<>(mJsonAdapter,type);
    }
}
