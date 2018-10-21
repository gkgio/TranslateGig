package com.gkgio.translate

import android.app.Application
import com.gkgio.translate.data.api.REST
import com.gkgio.translate.di.component.AppComponent
import com.gkgio.translate.di.component.DaggerAppComponent
import com.gkgio.translate.di.module.AppModule
import com.gkgio.translate.di.module.RoomModule

class AndroidApplication : Application() {

  companion object {
    lateinit var appComponent: AppComponent
  }

  override fun onCreate() {
    super.onCreate()
    initDagger()

    initREST()
  }

  private fun initDagger() {
    appComponent = DaggerAppComponent.builder()
        .appModule(AppModule(this))
        .roomModule(RoomModule(this))
        .build()
  }

  private fun initREST() {
    REST.instance = appComponent.api()
  }
}