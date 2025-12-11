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
import ru.tutu.tutuemployee.data.auth.*
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
 * Флаг для включения авторизации через Keycloak
 * true - использовать Keycloak
 * false - использовать простую авторизацию
 */
const val USE_KEYCLOAK = true

/**
 * DI модуль для сетевых компонентов
 */
val networkModule = module {
    // Token Storage (старый)
    single<TokenStorage> { InMemoryTokenStorage() }

    // Keycloak Token Storage
    single<KeycloakTokenStorage> { InMemoryKeycloakTokenStorage() }

    // Keycloak Config
    single<KeycloakConfig> {
        KeycloakConfig.getDefault()
    }

    // Keycloak HTTP Client (отдельный для Keycloak запросов)
    single<HttpClient>(qualifier = org.koin.core.qualifier.named("keycloak")) {
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

            install(HttpTimeout) {
                requestTimeoutMillis = 30000
                connectTimeoutMillis = 30000
                socketTimeoutMillis = 30000
            }
        }
    }

    // Keycloak Client
    single<KeycloakClient> {
        KeycloakClient(
            httpClient = get(qualifier = org.koin.core.qualifier.named("keycloak")),
            config = get(),
            tokenStorage = get()
        )
    }

    // Keycloak OAuth Handler
    single<KeycloakOAuthHandler> {
        KeycloakOAuthHandler(
            config = get(),
            keycloakClient = get()
        )
    }

    // HTTP Client (для API запросов)
    single<HttpClient>(qualifier = org.koin.core.qualifier.named("api")) {
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
                        val keycloakClient: KeycloakClient = get()
                        val tokenStorage: TokenStorage = get()

                        // Пробуем получить токен из Keycloak
                        val keycloakTokens = keycloakClient.isAuthenticated()
                        if (keycloakTokens) {
                            val keycloakTokenStorage: KeycloakTokenStorage = get()
                            keycloakTokenStorage.getTokens()?.let { tokens ->
                                return@loadTokens BearerTokens(
                                    accessToken = tokens.accessToken,
                                    refreshToken = tokens.refreshToken ?: tokens.accessToken
                                )
                            }
                        }

                        // Fallback на старый токен
                        tokenStorage.getToken()?.let {
                            BearerTokens(it, it)
                        }
                    }

                    refreshTokens {
                        val keycloakClient: KeycloakClient = get()
                        keycloakClient.refreshToken()
                            .map { tokens ->
                                BearerTokens(
                                    accessToken = tokens.accessToken,
                                    refreshToken = tokens.refreshToken ?: tokens.accessToken
                                )
                            }
                            .getOrNull()
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
            ApiServiceImpl(get(qualifier = org.koin.core.qualifier.named("api")))
        }
    }
}
