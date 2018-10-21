package com.gkgio.translate.features.splash.interactor

import com.gkgio.translate.AndroidApplication
import com.gkgio.translate.data.api.IService
import com.gkgio.translate.data.api.REST
import com.gkgio.translate.data.model.LanguagesResponse
import com.gkgio.translate.database.AppDatabase
import com.gkgio.translate.database.entities.Languages
import com.gkgio.translate.helpers.utils.Config
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.Callable
import javax.inject.Inject

class SplashInteractorImpl : SplashInteractor {

  @Inject
  lateinit var iService: IService

  @Inject
  lateinit var database: AppDatabase

  private val compositeDisposable: CompositeDisposable

  init {
    AndroidApplication.appComponent.inject(this)
    compositeDisposable = CompositeDisposable()
  }

  override fun fetchData(listener: SplashInteractor.OnFetchTranslateListener) {
    loadLanguageFromDatabase(listener)
  }

  private fun loadLanguageFromInternet(listener: SplashInteractor.OnFetchTranslateListener) {
    compositeDisposable.add(iService.getLangs(Config.API_KEY, Locale.getDefault().displayLanguage)
        .subscribeOn(Schedulers.io())
        .compose(REST.instance.errorHandler.cast())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ languagesResponse: LanguagesResponse ->
          val languages: HashMap<String, String>? = languagesResponse.langs
          if (languages != null) {
            listener.onSuccessFetchingLanguagesData(languages)
            writeLanguageToDatabase(languages)
          }
        }) { throwable: Throwable ->
          listener.onErrorFetchingLanguagesData(throwable)
        })
  }


  private fun loadLanguageFromDatabase(listener: SplashInteractor.OnFetchTranslateListener) {
    compositeDisposable.add(
        database.languagesDao().loadLanguages(1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ languages ->
              if (languages != null)
                listener.onSuccessFetchingLanguagesData(languages.languages)
            }) {
              loadLanguageFromInternet(listener)
            })
  }

  private fun writeLanguageToDatabase(languages: HashMap<String, String>) {
    val languagesObject = Languages(1, languages)
    compositeDisposable.add(Observable.fromCallable {
      database.languagesDao().insertLanguages(languagesObject)
    }.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
          //ignore
        })
  }

  override fun clearDisposables() {
    compositeDisposable.clear()
  }
}