package com.gkgio.translate.helpers.errorHandling


import com.gkgio.translate.BuildConfig
import com.gkgio.translate.di.scope.ActivityScope

import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

import javax.inject.Inject

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.functions.Function
import retrofit2.HttpException


@ActivityScope
class ErrorHandler @Inject
internal constructor(private val errorConverter: ErrorConverter,
                     private val errors: ApiErrors) : ObservableTransformer<Any, Any> {

  override fun apply(single: Observable<Any>): ObservableSource<Any> {
    return single.onErrorResumeNext(Function<Throwable, Observable<*>> { e ->
      val result: Throwable

      if (e is HttpException) {

        var error: ErrorDTO? = null
        try {
          error = errorConverter.getError(e)
        } catch (e1: Exception) {
          // ignore
        }

        val httpCode = e.code()
        val code = error?.code ?: -1

        result = ServerException(httpCode, error?.message ?: "", code)

      } else if (e is TimeoutException) {
        result = ConnectionException(errors.getMessage(ApiErrors.SERVER_CONNECT_TIMEOUT), e)
      } else if (isConnectionError(e)) {
        result = ConnectionException(errors.getMessage(ApiErrors.NO_CONNECTION), e)
      } else {
        result = UnexpectedError(errors.getMessage(ApiErrors.UNEXPECTED_ERROR), e)
      }

      if (BuildConfig.DEBUG) {
        result.printStackTrace()
      }

      Observable.error<Any>(result)
    })
  }

  @SuppressWarnings("unchecked")
  fun <T> cast(): ObservableTransformer<T, T> {
    return this as ObservableTransformer<T, T>
  }


  private fun isConnectionError(e: Throwable): Boolean {
    return e is TimeoutException ||
        e is UnknownHostException ||
        e is SocketTimeoutException ||
        e is java.net.ConnectException
  }
}
