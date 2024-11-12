package com.dirtfy.ppp.common.exception

object ExceptionRetryHandling {
    fun isRetry(error: Throwable?): Boolean {
        return when (error) {
            is ExternalException -> true
            else -> false
        }
    }
}