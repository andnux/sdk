package top.andnux.json;/*
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

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class JsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private Type mType;
    private JsonAdapter mJsonAdapter;

    public JsonResponseBodyConverter(JsonAdapter jsonAdapter, Type mType) {
        this.mJsonAdapter = jsonAdapter;
        this.mType = mType;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String json = value.string();
        return mJsonAdapter.parseObject(json, mType);
    }
}
