package top.andnux.net.retrofit.adapter

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import top.andnux.json.FastJsonProxy
import top.andnux.json.JsonProxy
import java.lang.reflect.Type

class JsonConverterFactory(private val mJsonProxy: JsonProxy) : Converter.Factory() {

    override fun responseBodyConverter(type: Type,
                                       annotations: Array<Annotation>?,
                                       retrofit: Retrofit?): Converter<ResponseBody, *> {
        return JsonResponseBodyConverter<Any>(mJsonProxy, type)
    }

    override fun requestBodyConverter(type: Type,
                                      parameterAnnotations: Array<Annotation>?,
                                      methodAnnotations: Array<Annotation>?,
                                      retrofit: Retrofit?): Converter<*, RequestBody> {
        return JsonResponseBodyConverter(mJsonProxy, type)
    }

    companion object {
        @JvmStatic
        @JvmOverloads
        fun create(jsonProxy: JsonProxy = FastJsonProxy()): JsonConverterFactory {
            return JsonConverterFactory(jsonProxy)
        }
    }
}
