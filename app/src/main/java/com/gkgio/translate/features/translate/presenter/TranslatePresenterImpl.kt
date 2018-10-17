package com.gkgio.translate.features.translate.presenter

import com.gkgio.translate.features.translate.interactor.TranslateInteractor
import com.gkgio.translate.features.translate.interactor.TranslateInteractorImpl
import com.gkgio.translate.features.translate.view.TranslateView

class TranslatePresenterImpl(translateView: TranslateView) : TranslatePresenter, TranslateInteractor.OnFetchTranslateListener {
  private var translateView: TranslateView? = translateView
  private var translateInteractor: TranslateInteractor = TranslateInteractorImpl()

  override fun fetchData(translateText: String) {
    translateInteractor.fetchData(translateText, this)
  }

  override fun onSuccessFetchingTranslateData(text: List<String>) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun onErrorFetchingTranslateData(error: Throwable) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }


  override fun onDestroy() {
    translateView = null
    translateInteractor.clearDisposables()
  }
}