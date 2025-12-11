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
import ru.tutu.tutuemployee.data.repository.ApiConfigRepository
import ru.tutu.tutuemployee.data.repository.ApiConfigRepositoryImpl
import ru.tutu.tutuemployee.data.repository.InMemoryTokenStorage
import ru.tutu.tutuemployee.data.repository.TokenStorage

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
                url("https://portal-service.hackaton2025-dv.svc.k8s.tutu.ru") // Реальный API URL
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 30000
                connectTimeoutMillis = 30000
                socketTimeoutMillis = 30000
            }
        }
    }

    // API Config Repository
    single<ApiConfigRepository> {
        ApiConfigRepositoryImpl()
    }

    // API Service - динамический выбор между моком и реальным API
    single<ApiService> {
        val apiConfigRepository: ApiConfigRepository = get()
        val httpClient: HttpClient = get(qualifier = org.koin.core.qualifier.named("api"))

        ApiServiceFactory(
            apiConfigRepository = apiConfigRepository,
            httpClient = httpClient
        )
    }
}

/**
 * Фабрика для создания ApiService, которая динамически переключается между моком и реальным API
 */
private class ApiServiceFactory(
    private val apiConfigRepository: ApiConfigRepository,
    private val httpClient: HttpClient
) : ApiService {
    private val mockService = MockApiService()
    private val realService = ApiServiceImpl(httpClient)

    private fun getService(): ApiService {
        return if (apiConfigRepository.useMockApi.value) {
            mockService
        } else {
            realService
        }
    }

    override suspend fun login(username: String, password: String) =
        getService().login(username, password)

    override suspend fun getUser(id: String) = getService().getUser(id)
    override suspend fun getNewsList(
        limit: Int?,
        offset: Int?,
        category: String?,
        search: String?
    ) =
        getService().getNewsList(limit, offset, category, search)

    override suspend fun getNewsItem(id: String) = getService().getNewsItem(id)
    override suspend fun searchEmployeesByLastName(lastName: String, limit: Int?, offset: Int?) =
        getService().searchEmployeesByLastName(lastName, limit, offset)

    override suspend fun getVacations(employeeId: String, year: Int?) =
        getService().getVacations(employeeId, year)

    override suspend fun getFavorites(userId: String) = getService().getFavorites(userId)
    override suspend fun addFavorite(request: ru.tutu.tutuemployee.data.remote.dto.AddFavoriteRequest) =
        getService().addFavorite(request)

    override suspend fun deleteFavorite(id: String) = getService().deleteFavorite(id)
    override suspend fun getAchievements(userId: String) = getService().getAchievements(userId)
    override suspend fun getCurrentUser() = getService().getCurrentUser()
    override suspend fun getNews() = getService().getNews()
    override suspend fun searchEmployees(query: String) = getService().searchEmployees(query)
    override suspend fun getBirthdays() = getService().getBirthdays()
    override suspend fun getTasks() = getService().getTasks()
    override suspend fun getCourses() = getService().getCourses()
    override suspend fun getWorkspaceBookings(date: String) =
        getService().getWorkspaceBookings(date)

    override suspend fun getOfficeNews() = getService().getOfficeNews()
    override suspend fun getMerchItems() = getService().getMerchItems()
    override suspend fun purchaseMerchItem(itemId: String) = getService().purchaseMerchItem(itemId)
}
