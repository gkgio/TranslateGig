package com.gkgio.translate.features.translate.presenter

import com.gkgio.translate.features.translate.interactor.TranslateInteractor
import com.gkgio.translate.features.translate.interactor.TranslateInteractorImpl
import com.gkgio.translate.features.translate.view.TranslateView

class TranslatePresenterImpl(translateView: TranslateView) : TranslatePresenter, TranslateInteractor.OnFetchTranslateListener {
  private var translateView: TranslateView? = translateView
  private var translateInteractor: TranslateInteractor = TranslateInteractorImpl()

  override fun fetchData(translateText: String, translateLanguage: String) {
    translateInteractor.fetchData(translateText, translateLanguage, this)
  }

  override fun onSuccessFetchingTranslateData(textList: List<String>) {
    translateView?.setTranslatedList(textList)
  }

  override fun onErrorFetchingTranslateData(error: Throwable) {
    translateView?.showErrorDialog(error.message ?: "")
  }

  override fun onDestroy() {
    translateView = null
    translateInteractor.clearDisposables()
  }
}