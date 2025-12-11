package ru.tutu.tutuemployee.data.auth

/**
 * Конфигурация Keycloak
 * Настройте эти параметры в соответствии с вашим Keycloak сервером
 */
data class KeycloakConfig(
    val serverUrl: String,
    val realm: String,
    val clientId: String,
    val clientSecret: String? = null, // Для confidential clients
    val redirectUri: String = "tutuemployee://oauth/callback"
) {
    companion object {
        /**
         * Получить дефолтную конфигурацию
         * В production рекомендуется загружать из переменных окружения или конфигурации
         */
        fun getDefault() = KeycloakConfig(
            serverUrl = "https://keycloak.tutu.ru",
            realm = "tutu",
            clientId = "dom-confluence",
            clientSecret = null
        )
    }

    /**
     * URL для получения токена (Token Endpoint)
     */
    val tokenEndpoint: String
        get() = "$serverUrl/realms/$realm/protocol/openid-connect/token"

    /**
     * URL для авторизации (Authorization Endpoint)
     */
    val authorizationEndpoint: String
        get() = "$serverUrl/realms/$realm/protocol/openid-connect/auth"

    /**
     * URL для получения информации о пользователе (UserInfo Endpoint)
     */
    val userInfoEndpoint: String
        get() = "$serverUrl/realms/$realm/protocol/openid-connect/userinfo"

    /**
     * URL для выхода (Logout Endpoint)
     */
    val logoutEndpoint: String
        get() = "$serverUrl/realms/$realm/protocol/openid-connect/logout"

    /**
     * URL для отзыва токена (Token Revocation Endpoint)
     */
    val revocationEndpoint: String
        get() = "$serverUrl/realms/$realm/protocol/openid-connect/revoke"

    /**
     * URL для получения конфигурации OpenID Connect (Discovery Endpoint)
     */
    val discoveryEndpoint: String
        get() = "$serverUrl/realms/$realm/.well-known/openid-configuration"
}
