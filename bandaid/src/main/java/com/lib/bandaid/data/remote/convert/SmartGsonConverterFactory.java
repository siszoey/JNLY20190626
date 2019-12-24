package com.lib.bandaid.data.remote.convert;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.lib.bandaid.data.remote.util.ResponseUtil;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class SmartGsonConverterFactory extends Converter.Factory {

    private final static Gson gson1 = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    private final static Gson gson2 = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();

    public static SmartGsonConverterFactory create() {
        return new SmartGsonConverterFactory();
    }

    @Nullable
    @Override

    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter1 = gson1.getAdapter(TypeToken.get(type));
        TypeAdapter<?> adapter2 = gson2.getAdapter(TypeToken.get(type));
        return new GsonResponseBodyConverter(gson1, adapter1, gson2, adapter2);
    }

    @Nullable
    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return super.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
    }

    private final static class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
        private final Gson gson1;
        private final Gson gson2;
        private final TypeAdapter<T> adapter1;
        private final TypeAdapter<T> adapter2;

        GsonResponseBodyConverter(Gson gson1, TypeAdapter<T> adapter1, Gson gson2, TypeAdapter<T> adapter2) {
            this.gson1 = gson1;
            this.gson2 = gson2;
            this.adapter1 = adapter1;
            this.adapter2 = adapter2;
        }

        @Override
        public T convert(ResponseBody value) throws IOException {
            ResponseBody[] bodies = ResponseUtil.deepCopy(value, 2);
            JsonReader jsonReader = gson1.newJsonReader(bodies[0].charStream());
            try {
                T entity = adapter1.read(jsonReader);
                return entity;
            } catch (Exception e) {
                String error = e.getMessage();
                if (error.contains("-") && error.contains(":") && error.contains("T")) {
                    jsonReader = gson2.newJsonReader(bodies[1].charStream());
                    T entity = adapter2.read(jsonReader);
                    return entity;
                }
                e.printStackTrace();
                throw e;
            } finally {
                value.close();
                ResponseUtil.closes(bodies);
            }
        }
    }

}
