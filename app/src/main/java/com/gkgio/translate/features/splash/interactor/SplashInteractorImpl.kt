package com.gkgio.translate.features.splash.interactor

import com.gkgio.translate.AndroidApplication
import com.gkgio.translate.data.api.IService
import com.gkgio.translate.data.api.REST
import com.gkgio.translate.data.model.LanguagesResponse
import com.gkgio.translate.helpers.utils.Config
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class SplashInteractorImpl : SplashInteractor {

  @Inject
  lateinit var iService: IService

  private val compositeDisposable: CompositeDisposable

  init {
    AndroidApplication.appComponent.inject(this)
    compositeDisposable = CompositeDisposable()
  }

  override fun fetchData(listener: SplashInteractor.OnFetchTranslateListener) {
    compositeDisposable.add(iService.getLangs(Config.API_KEY, Locale.getDefault().displayLanguage)
        .subscribeOn(Schedulers.io())
        .compose(REST.instance.errorHandler.cast())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ languagesResponse: LanguagesResponse ->
          val languages: HashMap<String, String>? = languagesResponse.langs
          if (languages != null)
            listener.onSuccessFetchingLanguagesData(languages)
        }) { throwable: Throwable ->
          listener.onErrorFetchingLanguagesData(throwable)
        })
  }

  override fun clearDisposables() {
    compositeDisposable.clear()
  }
}