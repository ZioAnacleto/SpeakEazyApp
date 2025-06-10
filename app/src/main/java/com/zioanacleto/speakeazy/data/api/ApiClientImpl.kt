package com.zioanacleto.speakeazy.data.api

import com.zioanacleto.speakeazy.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
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
import io.ktor.http.CacheControl
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.security.MessageDigest

class ApiClientImpl {

    private val _httpClient = HttpClient {
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

    suspend inline fun <reified T> executeGetRequest(
        url: String,
        isCached: Boolean = false
    ): T {
        return httpClient
            .get(url){
                headers {
                    if(isCached)
                        append(HttpHeaders.CacheControl, CacheControl.MaxAge(3600).toString())
                    append(HttpHeaders.Authorization, createAuthorizationHeader())
                }
            }.body()
    }

    suspend inline fun <reified T> executePutRequest(url: String, body: T? = null): Int {
        return httpClient.put(url) {
            headers {
                append(HttpHeaders.Authorization, createAuthorizationHeader())
            }
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(body))
        }.body<Int>()
    }

    suspend inline fun <reified Input, reified Output> executePostRequest(
        url: String,
        body: Input? = null
    ): Output {
        return httpClient.post(url) {
            headers {
                append(HttpHeaders.Authorization, createAuthorizationHeader())
            }
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(body))
            timeout {
                requestTimeoutMillis = 45*1000
            }
        }.body()
    }

    fun createAuthorizationHeader(): String {
        val apiKey = BuildConfig.API_KEY
        return "Bearer ${apiKey.hashToken()}"
    }

    private fun String.hashToken(): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(this.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}