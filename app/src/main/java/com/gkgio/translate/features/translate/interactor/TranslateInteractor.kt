package com.gkgio.translate.features.translate.interactor

interface TranslateInteractor {

  interface OnFetchTranslateListener {
    fun onSuccessFetchingTranslateData(text: List<String>)

    fun onErrorFetchingTranslateData(error: Throwable)
  }

  fun fetchData(translateText: String, listener: OnFetchTranslateListener)

  fun clearDisposables()
}