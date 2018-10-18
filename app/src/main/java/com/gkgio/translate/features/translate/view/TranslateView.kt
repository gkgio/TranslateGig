package com.gkgio.translate.features.translate.view

interface TranslateView {
  fun showErrorDialog(erorrMessage: String)

  fun setTranslatedList(textList: List<String>)
}