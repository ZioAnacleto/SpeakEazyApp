package com.zioanacleto.speakeazy.core.network.api

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
                CacheControl.MaxAge(ApiClientImpl.CACHE_MAX_AGE).toString(),
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
                content = "testResponse",
                headers = headersOf()
            )
        }

        // when
        val response = sut.executePostRequest(
            "",
            "testRequest",
            bodySerializer = String::class.serializer(),
            responseType = String::class
        )

        // then
        assert(response == "testResponse")
    }

    private fun createSut(
        block: MockRequestHandleScope.(HttpRequestData) -> HttpResponseData
    ) = ApiClientImpl(MockEngine { block(it) })

}