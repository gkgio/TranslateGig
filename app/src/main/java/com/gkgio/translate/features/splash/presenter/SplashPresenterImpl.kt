package com.gkgio.translate.features.splash.presenter

import com.gkgio.translate.features.splash.interactor.SplashInteractor
import com.gkgio.translate.features.splash.interactor.SplashInteractorImpl
import com.gkgio.translate.features.splash.view.SplashView
import java.util.HashMap

class SplashPresenterImpl(splashView: SplashView) : SplashPresenter, SplashInteractor.OnFetchTranslateListener {
  private var splashView: SplashView? = splashView
  private var splashInteractor: SplashInteractor = SplashInteractorImpl()

  override fun fetchData() {
    splashInteractor.fetchData(this)
  }

  override fun onSuccessFetchingLanguagesData(languages: HashMap<String, String>) {
    splashView?.openTranslateActivity(languages)
  }

  override fun onErrorFetchingLanguagesData(error: Throwable) {
    splashView?.showErrorDialog(error.message ?: "")
  }


  override fun onDestroy() {
    splashView = null
    splashInteractor.clearDisposables()
  }
}