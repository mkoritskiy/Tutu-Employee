package ru.tutu.tutuemployee.di

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.tutu.tutuemployee.data.remote.api.ApiService
import ru.tutu.tutuemployee.data.remote.api.ApiServiceImpl
import ru.tutu.tutuemployee.data.remote.api.MockApiService
import ru.tutu.tutuemployee.data.repository.InMemoryTokenStorage
import ru.tutu.tutuemployee.data.repository.TokenStorage

/**
 * Флаг для переключения между реальным и мок API
 * true - использовать мок данные
 * false - использовать реальный API
 */
const val USE_MOCK_API = true

/**
 * DI модуль для сетевых компонентов
 */
val networkModule = module {
    // Token Storage
    single<TokenStorage> { InMemoryTokenStorage() }

    // HTTP Client (только для реального API)
    single {
        HttpClient {
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
                        val tokenStorage: TokenStorage = get()
                        tokenStorage.getToken()?.let {
                            BearerTokens(it, it)
                        }
                    }
                }
            }

            install(DefaultRequest) {
                url("https://api.tutu.ru/employee") // Замените на реальный URL
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 30000
                connectTimeoutMillis = 30000
                socketTimeoutMillis = 30000
            }
        }
    }

    // API Service - выбираем между моком и реальным API
    single<ApiService> {
        if (USE_MOCK_API) {
            MockApiService()
        } else {
            ApiServiceImpl(get())
        }
    }
}
