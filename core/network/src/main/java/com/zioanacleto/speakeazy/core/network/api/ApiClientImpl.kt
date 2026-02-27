package com.zioanacleto.speakeazy.core.network.api

import com.zioanacleto.speakeazy.core.network.BuildConfig
import com.zioanacleto.speakeazy.core.network.model.ApiException
import com.zioanacleto.speakeazy.core.network.model.NoContentException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.plugins.timeout
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.CacheControl
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.reflect.TypeInfo
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import java.security.MessageDigest
import kotlin.reflect.KClass

class ApiClientImpl(
    engine: HttpClientEngine = CIO.create()
) {

    private val _httpClient = HttpClient(engine) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    useAlternativeNames = false
                    isLenient = true
                }
            )
        }
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
        }
        install(HttpCache)
        install(HttpTimeout)
    }

    val httpClient: HttpClient by lazy { _httpClient }

    suspend fun <T : Any> executeGetRequest(
        url: String,
        responseType: KClass<T>,
        isCached: Boolean = false,
        maxAgeSeconds: Int = CACHE_MAX_AGE_ONE_HOUR
    ): T {
        val response = httpClient
            .get(url) {
                headers {
                    if (isCached)
                        append(
                            HttpHeaders.CacheControl,
                            CacheControl.MaxAge(maxAgeSeconds).toString()
                        )
                    append(HttpHeaders.Authorization, createAuthorizationHeader())
                }
            }

        when (response.status) {
            HttpStatusCode.OK -> {
                return response.body(TypeInfo(responseType))
            }

            HttpStatusCode.NoContent -> {
                throw NoContentException("No content found.")
            }

            else -> {
                throw ApiException(
                    statusCode = response.status,
                    message = response.bodyAsText()
                )
            }
        }
    }

    suspend fun <T : Any> executePutRequest(
        url: String,
        body: T? = null,
        serializer: KSerializer<T>? = null
    ): Int {
        return httpClient.put(url) {
            headers {
                append(HttpHeaders.Authorization, createAuthorizationHeader())
            }
            contentType(ContentType.Application.Json)
            body?.let { requestBody ->
                serializer?.let { requestBodySerializer ->
                    setBody(Json.encodeToString(requestBodySerializer, requestBody))
                }
            }
        }.body<Int>()
    }

    suspend fun <Input : Any, Output : Any> executePostRequest(
        url: String,
        body: Input? = null,
        bodySerializer: KSerializer<Input>?,
        responseSerializer: KSerializer<Output>
    ): Output {
        val response = httpClient.post(url) {
            headers {
                append(HttpHeaders.Authorization, createAuthorizationHeader())
            }
            contentType(ContentType.Application.Json)
            body?.let { requestBody ->
                bodySerializer?.let { serializer ->
                    setBody(Json.encodeToString(serializer, requestBody))
                }
            }
            timeout {
                requestTimeoutMillis = REQUEST_TIMEOUT
            }
        }

        when (response.status) {
            HttpStatusCode.OK -> {
                return Json.decodeFromString(responseSerializer, response.bodyAsText())
            }

            HttpStatusCode.NoContent -> {
                throw NoContentException("No content found.")
            }

            else -> {
                throw ApiException(
                    statusCode = response.status,
                    message = response.bodyAsText()
                )
            }
        }
    }

    fun createAuthorizationHeader(): String {
        val apiKey = BuildConfig.API_KEY
        return "Bearer ${apiKey.hashToken()}"
    }

    private fun String.hashToken(): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(this.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    companion object {
        const val CACHE_MAX_AGE_ONE_HOUR = 1 * 60 * 60
        const val CACHE_MAX_AGE_FIVE_MINUTE = 5 * 60
        const val REQUEST_TIMEOUT = 45_000L
    }
}