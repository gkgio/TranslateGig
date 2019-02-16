package com.gkgio.translate.helpers.errorHandling

import com.google.gson.annotations.SerializedName

class ErrorDTO {
  @SerializedName("code")
  var code: Int = 0

  @SerializedName("message")
  var message: String? = null

}
