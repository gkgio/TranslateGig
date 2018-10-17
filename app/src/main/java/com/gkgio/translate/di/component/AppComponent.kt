package com.gkgio.translate.di.component

import dagger.Component
import com.gkgio.translate.di.module.AppModule
import com.gkgio.translate.data.api.IService
import com.gkgio.translate.data.api.REST
import com.gkgio.translate.di.scope.ActivityScope
import com.gkgio.translate.features.splash.interactor.SplashInteractorImpl
import com.gkgio.translate.features.translate.interactor.TranslateInteractorImpl
import com.gkgio.translate.helpers.errorHandilng.ApiErrors

@ActivityScope
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
  fun restService(): IService

  fun apiErrors(): ApiErrors

  fun api(): REST

  fun inject(translateInteractorImpl: TranslateInteractorImpl)

  fun inject(splashInteractorImpl: SplashInteractorImpl)
}