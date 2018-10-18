package com.gkgio.translate.features.translate.interactor

interface TranslateInteractor {

  interface OnFetchTranslateListener {
    fun onSuccessFetchingTranslateData(textList: List<String>)

    fun onErrorFetchingTranslateData(error: Throwable)
  }

  fun fetchData(translateText: String, translateLanguage: String, listener: OnFetchTranslateListener)

  fun clearDisposables()
}