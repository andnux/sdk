package top.andnux.net.retrofit.adapter

import androidx.lifecycle.LiveData
import com.google.gson.internal.`$Gson$Types`.getRawType
import retrofit2.CallAdapter
import retrofit2.CallAdapter.Factory
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class LiveDataCallAdapterFactory(private val clazz: Class<*>) : Factory() {
    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        if (getRawType(returnType) != LiveData::class) return null
        //获取第一个泛型类型
        val observableType = getParameterUpperBound(0, returnType as ParameterizedType)
        val rawType = getRawType(observableType)
        if (rawType != clazz) {
            throw IllegalArgumentException("type must be Response")
        }
        if (observableType !is ParameterizedType) {
            throw IllegalArgumentException("resource must be parameterized")
        }
        return LiveDataCallAdapter<Any>(clazz, observableType)
    }

    companion object {
        @JvmOverloads
        @JvmStatic
        fun create(clazz: Class<*> = Response::class.java): LiveDataCallAdapterFactory {
            return LiveDataCallAdapterFactory(clazz)
        }
    }
}