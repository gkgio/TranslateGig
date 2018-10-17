package com.gkgio.translate.features.translate.interactor

import com.gkgio.translate.AndroidApplication
import com.gkgio.translate.data.api.IService
import com.gkgio.translate.data.model.TranslateTextResponse
import com.gkgio.translate.helpers.utils.Config
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TranslateInteractorImpl : TranslateInteractor {

  @Inject
  lateinit var iService: IService

  private val compositeDisposable: CompositeDisposable

  init {
    AndroidApplication.appComponent.inject(this)
    compositeDisposable = CompositeDisposable()
  }

  override fun fetchData(translateText: String, listener: TranslateInteractor.OnFetchTranslateListener) {
    compositeDisposable.add(iService.translate("", Config.API_KEY, translateText)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ translateTextResponse: TranslateTextResponse ->
          if (translateTextResponse.text != null)
            listener.onSuccessFetchingTranslateData(translateTextResponse.text)
        }) { throwable: Throwable ->
          listener.onErrorFetchingTranslateData(throwable)
        })
  }


  override fun clearDisposables() {
    compositeDisposable.clear()
  }
}