package com.gkgio.translate.features.translate.view

interface TranslateView {
  fun showErrorDialog(errorMessage: String)

  fun setTranslatedList(textList: List<String>)
}