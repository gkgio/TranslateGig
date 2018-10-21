package com.gkgio.translate.di.module

import android.arch.persistence.room.Room
import com.gkgio.translate.AndroidApplication
import com.gkgio.translate.database.AppDatabase
import com.gkgio.translate.di.scope.ActivityScope
import dagger.Module
import dagger.Provides

@Module
class RoomModule(private val application: AndroidApplication) {

  @Provides
  @ActivityScope
  fun provideTranslateDatabase(): AppDatabase {
    return Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "Translate.db"
    ).build()
  }
}