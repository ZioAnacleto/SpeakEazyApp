package com.zioanacleto.speakeazy.core.data

import com.zioanacleto.speakeazy.core.network.api.ApiClientImpl
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import org.junit.Assert.assertTrue

/**
 *  This implies that all conditions must be true for the test to pass
 */
fun assertAllTrue(
    vararg condition: Boolean,
) = condition.forEachIndexed { index, it ->
    assertTrue("Condition ${index + 1} failed", it)
}

/**
 *  Utility function that mocks a [ApiClientImpl] with a [MockEngine], providing given response
 */
fun createApiClientWithResponse(
    status: HttpStatusCode,
    response: String
) =
    ApiClientImpl(
        MockEngine { _ ->
            respond(
                status = status,
                content = response,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
    )