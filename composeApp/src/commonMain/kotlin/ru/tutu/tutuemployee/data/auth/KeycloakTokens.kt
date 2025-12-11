package ru.tutu.tutuemployee.data.auth

import kotlinx.datetime.Clock
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Модель для хранения токенов Keycloak
 */
@Serializable
data class KeycloakTokens(
    @SerialName("access_token")
    val accessToken: String,

    @SerialName("refresh_token")
    val refreshToken: String? = null,

    @SerialName("id_token")
    val idToken: String? = null,

    @SerialName("token_type")
    val tokenType: String = "Bearer",

    @SerialName("expires_in")
    val expiresIn: Long,

    @SerialName("refresh_expires_in")
    val refreshExpiresIn: Long? = null,

    @SerialName("scope")
    val scope: String? = null
) {
    /**
     * Время создания токена (для расчета истечения срока, в миллисекундах)
     */
    @kotlinx.serialization.Transient
    var createdAtMs: Long = currentTimeMillis()

    /**
     * Проверка, истек ли access token
     * Добавляем буфер в 60 секунд для обновления до истечения
     */
    fun isAccessTokenExpired(): Boolean {
        val expiresAtMs = createdAtMs + (expiresIn * 1000)
        val nowMs = currentTimeMillis()
        val bufferMs = 60_000 // 60 seconds buffer
        return nowMs >= (expiresAtMs - bufferMs)
    }

    /**
     * Проверка, истек ли refresh token
     */
    fun isRefreshTokenExpired(): Boolean {
        if (refreshToken == null || refreshExpiresIn == null) return true
        val expiresAtMs = createdAtMs + (refreshExpiresIn * 1000)
        val nowMs = currentTimeMillis()
        return nowMs >= expiresAtMs
    }

    companion object {
        private fun currentTimeMillis(): Long = Clock.System.now().toEpochMilliseconds()
    }
}

/**
 * Ответ от Keycloak при обмене authorization code на токен
 */
@Serializable
data class TokenResponse(
    @SerialName("access_token")
    val accessToken: String,

    @SerialName("refresh_token")
    val refreshToken: String? = null,

    @SerialName("id_token")
    val idToken: String? = null,

    @SerialName("token_type")
    val tokenType: String,

    @SerialName("expires_in")
    val expiresIn: Long,

    @SerialName("refresh_expires_in")
    val refreshExpiresIn: Long? = null,

    @SerialName("scope")
    val scope: String? = null,

    @SerialName("session_state")
    val sessionState: String? = null
) {
    fun toKeycloakTokens() = KeycloakTokens(
        accessToken = accessToken,
        refreshToken = refreshToken,
        idToken = idToken,
        tokenType = tokenType,
        expiresIn = expiresIn,
        refreshExpiresIn = refreshExpiresIn,
        scope = scope
    )
}

/**
 * Информация о пользователе от Keycloak UserInfo endpoint
 */
@Serializable
data class KeycloakUserInfo(
    @SerialName("sub")
    val subject: String,

    @SerialName("preferred_username")
    val preferredUsername: String? = null,

    @SerialName("name")
    val name: String? = null,

    @SerialName("given_name")
    val givenName: String? = null,

    @SerialName("family_name")
    val familyName: String? = null,

    @SerialName("email")
    val email: String? = null,

    @SerialName("email_verified")
    val emailVerified: Boolean? = null,

    @SerialName("roles")
    val roles: List<String>? = null
)
