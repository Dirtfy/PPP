package com.dirtfy.ppp.common.exception

sealed class ExternalException(
    massage: String
): CustomException(massage) {
    class NetworkError: ExternalException("network error")
    class ServerError: ExternalException("server error")
    class UnknownError: ExternalException("unknown error")
}