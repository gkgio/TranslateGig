package com.gkgio.translate.features.translate.presenter

interface TranslatePresenter {
  fun fetchData(translateText: String, translateLanguage: String)

  fun onDestroy()
}