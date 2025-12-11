package ru.tutu.tutuemployee.data.auth

/**
 * Интерфейс для хранения токенов Keycloak
 * Абстракция для работы с локальным хранилищем токенов
 */
interface KeycloakTokenStorage {
    /**
     * Сохранить токены
     */
    fun saveTokens(tokens: KeycloakTokens)

    /**
     * Получить сохраненные токены
     */
    fun getTokens(): KeycloakTokens?

    /**
     * Очистить все токены
     */
    fun clearTokens()
}

/**
 * In-memory реализация хранилища токенов Keycloak
 *
 * ⚠️ ВАЖНО: В production рекомендуется использовать защищенное хранилище:
 * - Android: EncryptedSharedPreferences или DataStore с шифрованием
 * - iOS: Keychain
 * - Web: secure cookies или sessionStorage (не localStorage для чувствительных данных)
 * - Desktop: зашифрованный файл или системное хранилище
 */
class InMemoryKeycloakTokenStorage : KeycloakTokenStorage {
    private var tokens: KeycloakTokens? = null

    override fun saveTokens(tokens: KeycloakTokens) {
        this.tokens = tokens
    }

    override fun getTokens(): KeycloakTokens? = tokens

    override fun clearTokens() {
        tokens = null
    }
}
