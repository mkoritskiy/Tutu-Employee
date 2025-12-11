package ru.tutu.tutuemployee.data.auth

import io.ktor.http.*

/**
 * Обработчик OAuth авторизации через Keycloak
 * Генерирует URL для авторизации и обрабатывает callback
 */
class KeycloakOAuthHandler(
    private val config: KeycloakConfig,
    private val keycloakClient: KeycloakClient
) {
    private var currentCodeVerifier: String? = null
    private var currentState: String? = null

    /**
     * Создать URL для авторизации через браузер
     * @param scopes Список scope (по умолчанию: openid, profile, email)
     * @param usePKCE Использовать PKCE для дополнительной защиты (рекомендуется)
     * @return URL для редиректа пользователя
     */
    suspend fun createAuthorizationUrl(
        scopes: List<String> = listOf("openid", "profile", "email"),
        usePKCE: Boolean = true
    ): String {
        val state = PKCEHelper.generateState()
        currentState = state

        val urlBuilder = URLBuilder(config.authorizationEndpoint).apply {
            parameters.append("client_id", config.clientId)
            parameters.append("redirect_uri", config.redirectUri)
            parameters.append("response_type", "code")
            parameters.append("scope", scopes.joinToString(" "))
            parameters.append("state", state)

            if (usePKCE) {
                val codeVerifier = PKCEHelper.generateCodeVerifier()
                currentCodeVerifier = codeVerifier
                val codeChallenge = PKCEHelper.generateCodeChallenge(codeVerifier)
                parameters.append("code_challenge", codeChallenge)
                parameters.append("code_challenge_method", "S256")
            }
        }

        return urlBuilder.buildString()
    }

    /**
     * Обработать callback URL после авторизации
     * @param callbackUrl URL с параметрами code и state
     * @return Result с токенами или ошибкой
     */
    suspend fun handleAuthorizationCallback(callbackUrl: String): Result<KeycloakTokens> {
        val url = Url(callbackUrl)
        val code = url.parameters["code"]
        val state = url.parameters["state"]
        val error = url.parameters["error"]
        val errorDescription = url.parameters["error_description"]

        // Проверка на ошибки от Keycloak
        if (error != null) {
            return Result.failure(
                Exception("Authorization error: $error - ${errorDescription ?: "Unknown error"}")
            )
        }

        // Проверка наличия authorization code
        if (code == null) {
            return Result.failure(Exception("Missing authorization code"))
        }

        // Проверка state для защиты от CSRF
        if (state != currentState) {
            return Result.failure(Exception("Invalid state parameter - possible CSRF attack"))
        }

        // Обмен code на токены
        return keycloakClient.exchangeCodeForToken(
            code = code,
            codeVerifier = currentCodeVerifier
        ).also {
            // Очистка временных данных
            currentCodeVerifier = null
            currentState = null
        }
    }

    /**
     * Создать URL для логаута через Keycloak
     * @param postLogoutRedirectUri URL для редиректа после логаута
     * @param idToken ID token для hint (опционально)
     */
    fun createLogoutUrl(
        postLogoutRedirectUri: String? = null,
        idToken: String? = null
    ): String {
        val urlBuilder = URLBuilder(config.logoutEndpoint).apply {
            parameters.append("client_id", config.clientId)
            postLogoutRedirectUri?.let {
                parameters.append("post_logout_redirect_uri", it)
            }
            idToken?.let {
                parameters.append("id_token_hint", it)
            }
        }

        return urlBuilder.buildString()
    }

    /**
     * Очистить сохраненные данные OAuth сессии
     */
    fun clearOAuthData() {
        currentCodeVerifier = null
        currentState = null
    }
}
