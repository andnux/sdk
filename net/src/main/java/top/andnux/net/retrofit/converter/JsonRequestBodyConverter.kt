package top.andnux.net.retrofit.converter

/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException
import java.lang.reflect.Type
import java.nio.charset.Charset

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import retrofit2.Converter
import top.andnux.json.JsonProxy

internal class JsonRequestBodyConverter<T>(private val mJsonAdapter: JsonProxy, private val mType: Type) : Converter<T, RequestBody> {

    @Throws(IOException::class)
    override fun convert(value: T): RequestBody {
        val json = mJsonAdapter.toJSONString(value)
        return RequestBody.create(MEDIA_TYPE, json.toByteArray(UTF_8))
    }

    companion object {
        private val MEDIA_TYPE = "application/json; charset=UTF-8".toMediaType()
        private val UTF_8 = Charset.forName("UTF-8")
    }
}
