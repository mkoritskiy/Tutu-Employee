package ru.tutu.tutuemployee.domain.repository

import ru.tutu.tutuemployee.domain.model.User

/**
 * Repository interface для аутентификации
 */
interface AuthRepository {
    suspend fun login(username: String, password: String): Result<Pair<String, User>>
    suspend fun getCurrentUser(): Result<User>
    suspend fun logout()
}
