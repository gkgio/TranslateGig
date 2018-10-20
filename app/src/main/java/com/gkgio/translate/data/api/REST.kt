package com.gkgio.translate.data.api

import com.gkgio.translate.data.model.LanguagesResponse
import com.gkgio.translate.data.model.TranslateTextResponse
import com.gkgio.translate.di.scope.ActivityScope
import com.gkgio.translate.helpers.errorHandling.ErrorHandler
import com.gkgio.translate.helpers.utils.Config

import java.util.concurrent.TimeUnit

import javax.inject.Inject

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit

@ActivityScope
class REST @Inject
constructor(retrofit: Retrofit, val errorHandler: ErrorHandler) : IService {

  companion object {
    @JvmField
    val LOG_TAG: String = REST::class.java.toString()

    lateinit var instance: REST
  }

  private val service: IService = retrofit.create(IService::class.java)

  private fun <T> setup(): ObservableTransformer<T, T> {
    return setup(Config.TIMEOUT, Config.RETRIES)
  }

  private fun <T> setup(timeout: Int, retries: Int): ObservableTransformer<T, T> {
    return ObservableTransformer { observable ->
      observable
          .subscribeOn(Schedulers.io())
          .timeout(timeout.toLong(), TimeUnit.SECONDS)
          .retry(retries.toLong())
    }
  }

  override fun getLangs(apiKey: String, langCode: String): Observable<LanguagesResponse> {
    return service.getLangs(apiKey, langCode).compose(this.setup())
  }

  override fun translate(lang: String,
                         apiKey: String, text: String): Observable<TranslateTextResponse> {
    return service.translate(lang, apiKey, text).compose(this.setup())
  }
}
