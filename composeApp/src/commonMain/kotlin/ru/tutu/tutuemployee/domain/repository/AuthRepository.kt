package ru.tutu.tutuemployee.domain.repository

import ru.tutu.tutuemployee.domain.model.User

/**
 * Repository interface для аутентификации
 */
interface AuthRepository {
    /**
     * Авторизация через username/password (legacy метод)
     */
    suspend fun login(username: String, password: String): Result<Pair<String, User>>

    /**
     * Создать URL для OAuth авторизации
     */
    suspend fun createKeycloakAuthUrl(): Result<String>

    /**
     * Обработать OAuth callback
     */
    suspend fun handleKeycloakCallback(callbackUrl: String): Result<Pair<String, User>>

    /**
     * Получить текущего пользователя
     */
    suspend fun getCurrentUser(): Result<User>

    /**
     * Проверить, авторизован ли пользователь
     */
    fun isAuthenticated(): Boolean

    /**
     * Выход из системы
     */
    suspend fun logout()
}
