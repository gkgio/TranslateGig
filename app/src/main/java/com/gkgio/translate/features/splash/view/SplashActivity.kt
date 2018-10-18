package com.gkgio.translate.features.splash.view

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import com.gkgio.translate.R
import com.gkgio.translate.base.BaseActivity
import com.gkgio.translate.features.splash.presenter.SplashPresenter
import com.gkgio.translate.features.splash.presenter.SplashPresenterImpl
import com.gkgio.translate.features.translate.view.TranslateActivity
import com.gkgio.translate.helpers.utils.showErrorAlertDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.HashMap

class SplashActivity : BaseActivity(), SplashView {

  override val layoutRes: Int
    get() = R.layout.activity_splash

  private lateinit var splashPresenter: SplashPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    splashPresenter = SplashPresenterImpl(this)

    splashPresenter.fetchData()
  }

  override fun openTranslateActivity(languages: HashMap<String, String>) {
    val languagesMapType = object : TypeToken<HashMap<String, String>>() {}.type
    val languagesJsonString = Gson().toJson(languages, languagesMapType)
    val intent = Intent(this, TranslateActivity::class.java)
    intent.putExtra(TranslateActivity.ARG_LANGUAGES_AVAILABLE, languagesJsonString)
    startActivity(intent)
    finish()
  }

  override fun showErrorDialog(erorrMessage: String) {
    showErrorAlertDialog(
        this,
        erorrMessage,
        DialogInterface.OnClickListener { dialogInterface, i ->
          //nothing do
        })
  }

  override fun onDestroy() {
    super.onDestroy()
    splashPresenter.onDestroy()
  }
}