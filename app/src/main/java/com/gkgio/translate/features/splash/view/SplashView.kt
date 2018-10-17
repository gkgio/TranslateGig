package com.gkgio.translate.features.splash.view

import java.util.HashMap

interface SplashView {
  fun openTranslateActivity(languages: HashMap<String, String>)

  fun showErrorDialog(erorrMessage: String)
}