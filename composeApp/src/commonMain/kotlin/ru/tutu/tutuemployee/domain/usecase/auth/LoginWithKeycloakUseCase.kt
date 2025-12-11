package ru.tutu.tutuemployee.domain.usecase.auth

import ru.tutu.tutuemployee.domain.model.User
import ru.tutu.tutuemployee.domain.repository.AuthRepository

/**
 * Use case для авторизации через Keycloak (username/password)
 * Примечание: Этот метод использует Resource Owner Password Credentials Flow
 * и не рекомендуется для production. Используйте Authorization Code Flow вместо него.
 */
class LoginWithKeycloakUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(username: String, password: String): Result<Pair<String, User>> {
        if (username.isBlank()) {
            return Result.failure(IllegalArgumentException("Username cannot be empty"))
        }
        if (password.isBlank()) {
            return Result.failure(IllegalArgumentException("Password cannot be empty"))
        }

        return authRepository.loginWithKeycloak(username, password)
    }
}
