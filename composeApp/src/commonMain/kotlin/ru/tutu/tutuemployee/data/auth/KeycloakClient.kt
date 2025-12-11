package ru.tutu.tutuemployee.data.auth

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Клиент для работы с Keycloak OAuth2/OIDC
 */
class KeycloakClient(
    private val httpClient: HttpClient,
    private val config: KeycloakConfig,
    private val tokenStorage: KeycloakTokenStorage
) {
    private val refreshMutex = Mutex()

    /**
     * Обмен authorization code на токен (Authorization Code Flow)
     * Используется после редиректа с Keycloak
     */
    suspend fun exchangeCodeForToken(
        code: String,
        codeVerifier: String? = null // Для PKCE
    ): Result<KeycloakTokens> {
        return try {
            val response = httpClient.submitForm(
                url = config.tokenEndpoint,
                formParameters = parameters {
                    append("grant_type", "authorization_code")
                    append("client_id", config.clientId)
                    config.clientSecret?.let { append("client_secret", it) }
                    append("code", code)
                    append("redirect_uri", config.redirectUri)
                    codeVerifier?.let { append("code_verifier", it) }
                }
            ).body<TokenResponse>()

            val tokens = response.toKeycloakTokens()
            tokenStorage.saveTokens(tokens)
            Result.success(tokens)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Обновить access token используя refresh token
     */
    suspend fun refreshToken(): Result<KeycloakTokens> = refreshMutex.withLock {
        val currentTokens = tokenStorage.getTokens()
            ?: return Result.failure(IllegalStateException("No tokens available"))

        if (currentTokens.refreshToken == null) {
            return Result.failure(IllegalStateException("No refresh token available"))
        }

        if (currentTokens.isRefreshTokenExpired()) {
            tokenStorage.clearTokens()
            return Result.failure(IllegalStateException("Refresh token expired"))
        }

        return try {
            val response = httpClient.submitForm(
                url = config.tokenEndpoint,
                formParameters = parameters {
                    append("grant_type", "refresh_token")
                    append("client_id", config.clientId)
                    config.clientSecret?.let { append("client_secret", it) }
                    append("refresh_token", currentTokens.refreshToken)
                }
            ).body<TokenResponse>()

            val tokens = response.toKeycloakTokens()
            tokenStorage.saveTokens(tokens)
            Result.success(tokens)
        } catch (e: Exception) {
            tokenStorage.clearTokens()
            Result.failure(e)
        }
    }

    /**
     * Получить текущий валидный access token
     * Автоматически обновляет, если токен истек
     */
    suspend fun getValidAccessToken(): Result<String> {
        val tokens = tokenStorage.getTokens()
            ?: return Result.failure(IllegalStateException("Not authenticated"))

        // Если токен не истек, возвращаем его
        if (!tokens.isAccessTokenExpired()) {
            return Result.success(tokens.accessToken)
        }

        // Пытаемся обновить токен
        return refreshToken().map { it.accessToken }
    }

    /**
     * Получить информацию о пользователе
     */
    suspend fun getUserInfo(): Result<KeycloakUserInfo> {
        return try {
            val token = getValidAccessToken().getOrThrow()
            val response = httpClient.get(config.userInfoEndpoint) {
                bearerAuth(token)
            }.body<KeycloakUserInfo>()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Отозвать токен (logout)
     */
    suspend fun revokeToken(token: String): Result<Unit> {
        return try {
            httpClient.submitForm(
                url = config.revocationEndpoint,
                formParameters = parameters {
                    append("client_id", config.clientId)
                    config.clientSecret?.let { append("client_secret", it) }
                    append("token", token)
                }
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Выход из системы
     */
    suspend fun logout(): Result<Unit> {
        val tokens = tokenStorage.getTokens()
        tokenStorage.clearTokens()

        return try {
            if (tokens?.refreshToken != null) {
                revokeToken(tokens.refreshToken)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            // Игнорируем ошибки, т.к. токены уже очищены локально
            Result.success(Unit)
        }
    }

    /**
     * Проверить, авторизован ли пользователь
     */
    fun isAuthenticated(): Boolean {
        val tokens = tokenStorage.getTokens() ?: return false
        return !tokens.isRefreshTokenExpired()
    }
}
