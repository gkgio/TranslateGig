package com.gkgio.translate.helpers.errorHandling


class ConnectionException internal constructor(message: String, cause: Throwable)
  : RuntimeException(message, cause)
