package com.gkgio.translate.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.gkgio.translate.database.daos.LanguagesDao
import com.gkgio.translate.database.entities.Languages

@Database(entities = [(Languages::class)], version = 5, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
  abstract fun languagesDao(): LanguagesDao
}