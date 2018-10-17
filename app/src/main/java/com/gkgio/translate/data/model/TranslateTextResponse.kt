package com.gkgio.translate.data.model

import com.google.gson.annotations.SerializedName

class TranslateTextResponse(
    @SerializedName("code")
    val code: Int,

    @SerializedName("lang")
    val lang: String,

    @SerializedName("text")
    val text: List<String>?
)
