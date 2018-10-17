package com.gkgio.translate.helpers.errorHandilng;

import com.gkgio.translate.di.scope.ActivityScope;

import java.io.IOException;
import java.lang.annotation.Annotation;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.HttpException;
import retrofit2.Retrofit;

@ActivityScope
public class ErrorConverter {

    private final Converter<ResponseBody, ErrorDTO> converter;

    @Inject
    public ErrorConverter(Retrofit retrofit) {
        converter = retrofit.responseBodyConverter(ErrorDTO.class, new Annotation[0]);
    }

    public ErrorDTO getError(HttpException e) throws IOException {
        return converter.convert(e.response().errorBody());
    }

}
