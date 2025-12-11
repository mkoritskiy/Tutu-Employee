package ru.tutu.tutuemployee.data.remote.datasource

import ru.tutu.tutuemployee.data.remote.api.ApiService
import ru.tutu.tutuemployee.data.remote.dto.AuthResponse
import ru.tutu.tutuemployee.data.remote.dto.UserDto

/**
 * Remote data source для работы с аутентификацией
 */
interface AuthRemoteDataSource {
    suspend fun login(username: String, password: String): Result<AuthResponse>
    suspend fun getCurrentUser(): Result<UserDto>
}

class AuthRemoteDataSourceImpl(
    private val apiService: ApiService
) : AuthRemoteDataSource {

    override suspend fun login(username: String, password: String): Result<AuthResponse> {
        return apiService.login(username, password)
    }

    override suspend fun getCurrentUser(): Result<UserDto> {
        return apiService.getCurrentUser()
    }
}
