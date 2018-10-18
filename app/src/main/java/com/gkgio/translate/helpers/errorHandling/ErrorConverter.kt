package com.gkgio.translate.helpers.errorHandling

import com.gkgio.translate.di.scope.ActivityScope

import java.io.IOException

import javax.inject.Inject

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.HttpException
import retrofit2.Retrofit

@ActivityScope
class ErrorConverter @Inject
constructor(retrofit: Retrofit) {

  private val converter: Converter<ResponseBody, ErrorDTO> =
      retrofit.responseBodyConverter(ErrorDTO::class.java, arrayOfNulls(0))

  @Throws(IOException::class)
  fun getError(e: HttpException): ErrorDTO {
    return converter.convert(e.response().errorBody()!!)
  }

}
