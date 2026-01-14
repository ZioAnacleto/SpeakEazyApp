package com.zioanacleto.speakeazy

import com.zioanacleto.speakeazy.data.api.ApiClientImpl
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.zip
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
 *  Utility function that retrieves first value and second value after [dropCount] emissions
 *  and return them inside a [Pair]
 */
suspend fun <T> Flow<T>.testResourceFlow(
    dropCount: Int = 1
): Pair<T, T> = this.zip(this.drop(dropCount)) { first, second ->
    first to second
}.first()

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