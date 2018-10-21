package com.gkgio.translate.database.daos

import android.arch.persistence.room.*
import com.gkgio.translate.database.entities.Languages
import io.reactivex.Single

@Dao
interface LanguagesDao {

  @Query("SELECT * FROM languages WHERE id = :id")
  fun loadLanguages(id: Int): Single<Languages>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertLanguages(languages: Languages)

  @Delete
  fun deleteLanguages(languages: Languages)
}