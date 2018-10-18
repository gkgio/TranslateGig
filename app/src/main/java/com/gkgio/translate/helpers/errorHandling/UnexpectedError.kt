package com.gkgio.translate.helpers.errorHandling


class UnexpectedError(message: String, cause: Throwable) : RuntimeException(message, cause)
