package com.gkgio.translate.data.api

import com.gkgio.translate.data.model.*
import io.reactivex.Observable
import retrofit2.http.POST
import retrofit2.http.Query

interface IService {
  @POST("getLangs")
  fun getLangs(@Query("key") apiKey: String, @Query("ui") langCode: String): Observable<LanguagesResponse>

  @POST("translate")
  fun translate(@Query("lang") lang: String,
                @Query("key") apiKey: String,
                @Query("text") text: String): Observable<TranslateTextResponse>
}
