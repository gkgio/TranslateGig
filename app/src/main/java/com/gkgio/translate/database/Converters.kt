package com.gkgio.translate.database

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
  @TypeConverter
  fun hashMapToJson(value: HashMap<String, String>?): String {
    return Gson().toJson(value)
  }

  @TypeConverter
  fun jsonToHashMap(value: String): HashMap<String, String>? {
    val gson = Gson()
    val hashMapType = object : TypeToken<HashMap<String, String>>() {}.type
    return gson.fromJson(value, hashMapType)
  }
}