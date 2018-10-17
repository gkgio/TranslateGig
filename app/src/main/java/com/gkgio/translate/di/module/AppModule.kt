package com.gkgio.translate.di.module

import android.content.Context
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import com.gkgio.translate.AndroidApplication
import com.gkgio.translate.BuildConfig
import com.gkgio.translate.data.api.IService
import com.gkgio.translate.di.scope.ActivityScope
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class AppModule(private val application: AndroidApplication) {
  @Provides
  @Singleton
  fun provideContext(): Context = application.applicationContext

  @Provides
  @ActivityScope
  fun provideResources(): Resources = application.resources

  @Provides
  @ActivityScope
  fun provideHttpLogging(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor().setLevel(if (BuildConfig.DEBUG)
      HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)
  }

  @Provides
  @ActivityScope
  fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .readTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .followSslRedirects(true)
        .build()
  }

  @Provides
  @ActivityScope
  fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.API_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
  }

  @Provides
  @ActivityScope
  fun provideRestService(retrofit: Retrofit): IService = retrofit.create(IService::class.java)
}