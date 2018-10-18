package com.gkgio.translate.helpers.errorHandling


class ServerException internal constructor(val httpCode: Int, message: String,
                                           val code: Int) : RuntimeException(message)
