package com.gkgio.translate.features.splash.interactor

import java.util.HashMap

interface SplashInteractor {
  interface OnFetchTranslateListener {
    fun onSuccessFetchingLanguagesData(languages: HashMap<String, String>)

    fun onErrorFetchingLanguagesData(error: Throwable)
  }

  fun fetchData(listener: OnFetchTranslateListener)

  fun clearDisposables()
}