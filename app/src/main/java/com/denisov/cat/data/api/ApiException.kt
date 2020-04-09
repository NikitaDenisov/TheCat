package com.denisov.cat.data.api

sealed class ApiException(message: String? = null, cause: Throwable? = null) : Exception(message, cause) {
    class Communication(cause: Throwable? = null) : ApiException(cause = cause)
    class Connection(cause: Throwable? = null) : ApiException(cause = cause)
}