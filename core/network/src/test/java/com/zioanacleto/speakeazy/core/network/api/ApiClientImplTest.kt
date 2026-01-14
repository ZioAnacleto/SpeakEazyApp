package com.zioanacleto.speakeazy.core.network.api

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpResponseData
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.mockk.clearAllMocks
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test

class ApiClientImplTest {

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `executeGetRequest - success should return data`() = runBlocking {
        // given
        val sut = createSut {
            respond(
                status = HttpStatusCode.OK,
                content = "test",
                headers = headersOf()
            )
        }

        // when
        val response = sut.executeGetRequest<String>("", true)

        // then
        assert(response == "test")
    }

    @Test
    fun `executePutRequest - success should return data`() = runBlocking {
        // given
        val sut = createSut {
            respond(
                status = HttpStatusCode.OK,
                content = "1",
                headers = headersOf()
            )
        }

        // when
        val response = sut.executePutRequest("", "test")

        // then
        assert(response == 1)
    }

    @Test
    fun `executePostRequest - success should return data`() = runBlocking {
        // given
        val sut = createSut {
            respond(
                status = HttpStatusCode.OK,
                content = "testResponse",
                headers = headersOf()
            )
        }

        // when
        val response = sut.executePostRequest<String, String>("", "testRequest")

        // then
        assert(response == "testResponse")
    }

    private fun createSut(
        block: MockRequestHandleScope.() -> HttpResponseData
    ) = ApiClientImpl(MockEngine { block() })

}