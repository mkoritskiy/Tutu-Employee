package ru.tutu.tutuemployee.data.repository

import ru.tutu.tutuemployee.data.remote.datasource.AuthRemoteDataSource
import ru.tutu.tutuemployee.data.remote.dto.toDomain
import ru.tutu.tutuemployee.domain.model.User
import ru.tutu.tutuemployee.domain.repository.AuthRepository

/**
 * Реализация AuthRepository
 * Использует Clean Architecture принципы:
 * - Зависит только от интерфейсов (AuthRemoteDataSource)
 * - Преобразует DTO в Domain модели
 * - Обрабатывает бизнес-логику на уровне репозитория
 */
class AuthRepositoryImpl(
    private val remoteDataSource: AuthRemoteDataSource,
    private val tokenStorage: TokenStorage
) : AuthRepository {

    override suspend fun login(username: String, password: String): Result<Pair<String, User>> {
        return remoteDataSource.login(username, password)
            .mapCatching { authResponse ->
                tokenStorage.saveToken(authResponse.token)
                Pair(authResponse.token, authResponse.user.toDomain())
            }
    }

    override suspend fun getCurrentUser(): Result<User> {
        return remoteDataSource.getCurrentUser()
            .map { it.toDomain() }
    }

    override suspend fun logout() {
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
