package ru.tutu.tutuemployee.domain.usecase.auth

import ru.tutu.tutuemployee.domain.model.User
import ru.tutu.tutuemployee.domain.repository.AuthRepository

/**
 * Use case для авторизации пользователя
 */
class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(username: String, password: String): Result<Pair<String, User>> {
        if (username.isBlank()) {
            return Result.failure(IllegalArgumentException("Username cannot be empty"))
        }
        if (password.isBlank()) {
            return Result.failure(IllegalArgumentException("Password cannot be empty"))
        }

        return authRepository.login(username, password)
    }
}
