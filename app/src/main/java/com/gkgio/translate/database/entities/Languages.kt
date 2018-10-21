package com.gkgio.translate.database.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "languages")
class Languages constructor(id: Int, languages: HashMap<String, String>) {

  @PrimaryKey
  var id: Int = id

  @ColumnInfo(name = "languages_list")
  var languages: HashMap<String, String> = languages
}