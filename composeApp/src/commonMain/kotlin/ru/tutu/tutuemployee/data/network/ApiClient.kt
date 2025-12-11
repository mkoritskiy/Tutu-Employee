package ru.tutu.tutuemployee.data.network

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object ApiClient {
    private const val BASE_URL = "https://api.tutu.ru/employee" // Замените на реальный URL

    private var authToken: String? = null

    fun setAuthToken(token: String?) {
        authToken = token
    }

    fun getAuthToken(): String? = authToken

    val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                prettyPrint = true
            })
        }

        install(Logging) {
            level = LogLevel.INFO
        }

        install(Auth) {
            bearer {
                loadTokens {
                    authToken?.let {
                        BearerTokens(it, it)
                    }
                }
            }
        }

        install(DefaultRequest) {
            url(BASE_URL)
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 30000
            connectTimeoutMillis = 30000
            socketTimeoutMillis = 30000
        }
    }
}
