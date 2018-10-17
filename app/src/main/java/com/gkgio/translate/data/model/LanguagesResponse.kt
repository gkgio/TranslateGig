package com.gkgio.translate.data.model

import com.google.gson.annotations.SerializedName

class LanguagesResponse(
    @SerializedName("dirs")
    val dirs: List<String>?,

    @SerializedName("langs")
    val langs: HashMap<String, String>?
)