package com.gkgio.translate.helpers.errorHandling

import com.google.gson.annotations.SerializedName

/**
 * {"code":6,"message":"Path not found"}
 */
class ErrorDTO {
  @SerializedName("code")
  var code: Int = 0

  @SerializedName("message")
  var message: String? = null

}
