package ru.tutu.tutuemployee.data.repository

import ru.tutu.tutuemployee.data.auth.KeycloakClient
import ru.tutu.tutuemployee.data.auth.KeycloakOAuthHandler
import ru.tutu.tutuemployee.data.remote.datasource.AuthRemoteDataSource
import ru.tutu.tutuemployee.data.remote.dto.toDomain
import ru.tutu.tutuemployee.domain.model.User
import ru.tutu.tutuemployee.domain.repository.AuthRepository

/**
 * Реализация AuthRepository с поддержкой Keycloak
 * Использует Clean Architecture принципы:
 * - Зависит только от интерфейсов (AuthRemoteDataSource)
 * - Преобразует DTO в Domain модели
 * - Обрабатывает бизнес-логику на уровне репозитория
 */
class AuthRepositoryImpl(
    private val remoteDataSource: AuthRemoteDataSource,
    private val tokenStorage: TokenStorage,
    private val keycloakClient: KeycloakClient,
    private val keycloakOAuthHandler: KeycloakOAuthHandler
) : AuthRepository {

    override suspend fun login(username: String, password: String): Result<Pair<String, User>> {
        return remoteDataSource.login(username, password)
            .mapCatching { authResponse ->
                tokenStorage.saveToken(authResponse.token)
                Pair(authResponse.token, authResponse.user.toDomain())
            }
    }

    override suspend fun loginWithKeycloak(
        username: String,
        password: String
    ): Result<Pair<String, User>> {
        return keycloakClient.loginWithPassword(username, password)
            .mapCatching { tokens ->
                // Сохраняем access token в старое хранилище для обратной совместимости
                tokenStorage.saveToken(tokens.accessToken)

                // Получаем информацию о пользователе
                val userInfo = keycloakClient.getUserInfo().getOrThrow()

                // Преобразуем в доменную модель
                val user = User(
                    id = userInfo.subject,
                    username = userInfo.preferredUsername ?: userInfo.email ?: userInfo.subject,
                    email = userInfo.email ?: "",
                    firstName = userInfo.givenName ?: "",
                    lastName = userInfo.familyName ?: "",
                    position = "", // Получать из атрибутов Keycloak или отдельного API
                    department = "", // Получать из атрибутов Keycloak или отдельного API
                    legalEntity = "", // Получать из атрибутов Keycloak или отдельного API
                    avatarUrl = null,
                    availableVacationDays = 0,
                    bonusPoints = 0
                )

                Pair(tokens.accessToken, user)
            }
    }

    override suspend fun createKeycloakAuthUrl(): Result<String> {
        return try {
            val url = keycloakOAuthHandler.createAuthorizationUrl()
            Result.success(url)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun handleKeycloakCallback(callbackUrl: String): Result<Pair<String, User>> {
        return keycloakOAuthHandler.handleAuthorizationCallback(callbackUrl)
            .mapCatching { tokens ->
                // Сохраняем access token в старое хранилище для обратной совместимости
                tokenStorage.saveToken(tokens.accessToken)

                // Получаем информацию о пользователе
                val userInfo = keycloakClient.getUserInfo().getOrThrow()

                // Преобразуем в доменную модель
                val user = User(
                    id = userInfo.subject,
                    username = userInfo.preferredUsername ?: userInfo.email ?: userInfo.subject,
                    email = userInfo.email ?: "",
                    firstName = userInfo.givenName ?: "",
                    lastName = userInfo.familyName ?: "",
                    position = "", // Получать из атрибутов Keycloak или отдельного API
                    department = "", // Получать из атрибутов Keycloak или отдельного API
                    legalEntity = "", // Получать из атрибутов Keycloak или отдельного API
                    avatarUrl = null,
                    availableVacationDays = 0,
                    bonusPoints = 0
                )

                Pair(tokens.accessToken, user)
            }
    }

    override suspend fun getCurrentUser(): Result<User> {
        // Пробуем сначала через Keycloak, если не получилось - через старый API
        return keycloakClient.getUserInfo()
            .mapCatching { userInfo ->
                User(
                    id = userInfo.subject,
                    username = userInfo.preferredUsername ?: userInfo.email ?: userInfo.subject,
                    email = userInfo.email ?: "",
                    firstName = userInfo.givenName ?: "",
                    lastName = userInfo.familyName ?: "",
                    position = "", // Получать из атрибутов Keycloak или отдельного API
                    department = "", // Получать из атрибутов Keycloak или отдельного API
                    legalEntity = "", // Получать из атрибутов Keycloak или отдельного API
                    avatarUrl = null,
                    availableVacationDays = 0,
                    bonusPoints = 0
                )
            }
            .recoverCatching {
                // Fallback на старый API
                remoteDataSource.getCurrentUser().map { it.toDomain() }.getOrThrow()
            }
    }

    override fun isAuthenticated(): Boolean {
        return keycloakClient.isAuthenticated() || tokenStorage.getToken() != null
    }

    override suspend fun logout() {
        keycloakClient.logout()
        keycloakOAuthHandler.clearOAuthData()
        tokenStorage.clearToken()
    }
}

/**
 * Интерфейс для хранения токена
 * Абстракция для работы с локальным хранилищем токенов
 */
interface TokenStorage {
    fun saveToken(token: String)
    fun getToken(): String?
    fun clearToken()
}

/**
 * In-memory реализация хранилища токенов
 * В production рекомендуется использовать:
 * - Android: DataStore/SharedPreferences
 * - iOS: UserDefaults/Keychain
 * - Web: localStorage/sessionStorage
 */
class InMemoryTokenStorage : TokenStorage {
    private var token: String? = null

    override fun saveToken(token: String) {
        this.token = token
    }

    override fun getToken(): String? = token

    override fun clearToken() {
        token = null
    }
}
