package com.zioanacleto.speakeazy.core.network.api

import com.zioanacleto.speakeazy.core.network.model.ApiException
import com.zioanacleto.speakeazy.core.network.model.NoContentException
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.HttpTimeoutCapability
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.http.CacheControl
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.mockk.clearAllMocks
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(InternalSerializationApi::class)
class ApiClientImplTest {

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `executeGetRequest - success should return cached data`() = runBlocking {
        // given
        val sut = createSut {request ->
            // Cache control header
            val cacheControl = request.headers[HttpHeaders.CacheControl]
            assertEquals(
                CacheControl.MaxAge(ApiClientImpl.CACHE_MAX_AGE_ONE_HOUR).toString(),
                cacheControl
            )
            respond(
                status = HttpStatusCode.OK,
                content = "test",
                headers = headersOf()
            )
        }

        // when
        val response = sut.executeGetRequest("", String::class, true)

        // then
        assert(response == "test")
    }

    @Test
    fun `executeGetRequest - success should return not cached data`() = runBlocking {
        // given
        val sut = createSut {request ->
            // Cache Control header
            val cacheControl = request.headers[HttpHeaders.CacheControl]
            assertEquals(
                null,
                cacheControl
            )

            // Authorization header
            val authHeader = request.headers[HttpHeaders.Authorization]

            assert(authHeader != null)
            assertTrue(authHeader?.startsWith("Bearer ") == true)

            respond(
                status = HttpStatusCode.OK,
                content = "test",
                headers = headersOf()
            )
        }

        // when
        val response = sut.executeGetRequest("", String::class, false)

        // then
        assert(response == "test")
    }

    @Test
    fun `executeGetRequest - NoContent should throw NoContentException`() = runBlocking {
        // given
        var isExceptionThrown = false
        val sut = createSut {request ->
            // Cache Control header
            val cacheControl = request.headers[HttpHeaders.CacheControl]
            assertEquals(
                null,
                cacheControl
            )

            // Authorization header
            val authHeader = request.headers[HttpHeaders.Authorization]

            assert(authHeader != null)
            assertTrue(authHeader?.startsWith("Bearer ") == true)

            respond(
                status = HttpStatusCode.NoContent,
                content = "test",
                headers = headersOf()
            )
        }

        // when
        try {
            sut.executeGetRequest("", String::class, false)
        } catch (_: NoContentException) {
            isExceptionThrown = true
        }

        // then
        assertTrue(isExceptionThrown)
    }

    @Test
    fun `executeGetRequest - other status should throw ApiException`() = runBlocking {
        // given
        var isExceptionThrown = false
        val sut = createSut {request ->
            // Cache Control header
            val cacheControl = request.headers[HttpHeaders.CacheControl]
            assertEquals(
                null,
                cacheControl
            )

            // Authorization header
            val authHeader = request.headers[HttpHeaders.Authorization]

            assert(authHeader != null)
            assertTrue(authHeader?.startsWith("Bearer ") == true)

            respond(
                status = HttpStatusCode.BadGateway,
                content = "test",
                headers = headersOf()
            )
        }

        // when
        try {
            sut.executeGetRequest("", String::class, false)
        } catch (_: ApiException) {
            isExceptionThrown = true
        }

        // then
        assertTrue(isExceptionThrown)
    }

    @Test
    fun `executePutRequest - success should return data`() = runBlocking {
        // given
        val sut = createSut {request ->
            val authHeader = request.headers[HttpHeaders.Authorization]

            assert(authHeader != null)
            assertTrue(authHeader?.startsWith("Bearer ") == true)

            respond(
                status = HttpStatusCode.OK,
                content = "1",
                headers = headersOf()
            )
        }

        // when
        val response = sut.executePutRequest("", "test", String::class.serializer())

        // then
        assert(response == 1)
    }

    @Test
    fun `executePostRequest - success should return data`() = runBlocking {
        // given
        val sut = createSut {request ->
            // Authorization header
            val authHeader = request.headers[HttpHeaders.Authorization]

            assert(authHeader != null)
            assertTrue(authHeader?.startsWith("Bearer ") == true)

            // Timeout capability
            val timeout = request.getCapabilityOrNull(HttpTimeoutCapability)?.requestTimeoutMillis

            assert(timeout != null)
            assertEquals(timeout, ApiClientImpl.REQUEST_TIMEOUT)

            respond(
                status = HttpStatusCode.OK,
                content = Json.encodeToString("testResponse"),
                headers = headersOf()
            )
        }

        // when
        val response = sut.executePostRequest(
            "",
            "testRequest",
            bodySerializer = String::class.serializer(),
            responseSerializer = String::class.serializer()
        )

        // then
        assert(response == "testResponse")
    }

    @Test
    fun `executePostRequest - NoContent status should throw NoContentException`() = runBlocking {
        // given
        var isExceptionThrown = false
        val sut = createSut {request ->
            // Authorization header
            val authHeader = request.headers[HttpHeaders.Authorization]

            assert(authHeader != null)
            assertTrue(authHeader?.startsWith("Bearer ") == true)

            // Timeout capability
            val timeout = request.getCapabilityOrNull(HttpTimeoutCapability)?.requestTimeoutMillis

            assert(timeout != null)
            assertEquals(timeout, ApiClientImpl.REQUEST_TIMEOUT)

            respond(
                status = HttpStatusCode.NoContent,
                content = Json.encodeToString("testResponse"),
                headers = headersOf()
            )
        }

        // when
        try {
            sut.executePostRequest(
                "",
                "testRequest",
                bodySerializer = String::class.serializer(),
                responseSerializer = String::class.serializer()
            )
        } catch (_: NoContentException) {
            isExceptionThrown = true
        }

        // then
        assertTrue(isExceptionThrown)
    }

    @Test
    fun `executePostRequest - other status should throw ApiException`() = runBlocking {
        // given
        var isExceptionThrown = false
        val sut = createSut {request ->
            // Authorization header
            val authHeader = request.headers[HttpHeaders.Authorization]

            assert(authHeader != null)
            assertTrue(authHeader?.startsWith("Bearer ") == true)

            // Timeout capability
            val timeout = request.getCapabilityOrNull(HttpTimeoutCapability)?.requestTimeoutMillis

            assert(timeout != null)
            assertEquals(timeout, ApiClientImpl.REQUEST_TIMEOUT)

            respond(
                status = HttpStatusCode.BadGateway,
                content = Json.encodeToString("testResponse"),
                headers = headersOf()
            )
        }

        // when
        try {
            sut.executePostRequest(
                "",
                "testRequest",
                bodySerializer = String::class.serializer(),
                responseSerializer = String::class.serializer()
            )
        } catch (_: ApiException) {
            isExceptionThrown = true
        }

        // then
        assertTrue(isExceptionThrown)
    }

    private fun createSut(
        block: MockRequestHandleScope.(HttpRequestData) -> HttpResponseData
    ) = ApiClientImpl(MockEngine { block(it) })

}