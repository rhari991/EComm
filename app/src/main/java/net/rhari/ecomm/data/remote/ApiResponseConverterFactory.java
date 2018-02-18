package net.rhari.ecomm.data.remote;

import net.rhari.ecomm.data.model.ApiResponse;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

class ApiResponseConverterFactory extends Converter.Factory {

    static ApiResponseConverterFactory create() {
        return new ApiResponseConverterFactory();
    }

    private ApiResponseConverterFactory() {

    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (getRawType(type) == ApiResponse.class) {
            return ApiResponseConverter.getInstance();
        }
        return null;
    }
}
