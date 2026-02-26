package com.zioanacleto.speakeazy.core.network.model

import io.ktor.http.HttpStatusCode

// 204 Status Code
class NoContentException(message: String): Exception(message)

// Generic error
class ApiException(
    val statusCode: HttpStatusCode,
    message: String
) : Exception(message)