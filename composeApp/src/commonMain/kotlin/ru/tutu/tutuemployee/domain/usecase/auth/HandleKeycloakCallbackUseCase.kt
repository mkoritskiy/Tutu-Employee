package ru.tutu.tutuemployee.domain.usecase.auth

import ru.tutu.tutuemployee.domain.model.User
import ru.tutu.tutuemployee.domain.repository.AuthRepository

/**
 * Use case для обработки callback URL от Keycloak
 * Используется после редиректа с Keycloak в Authorization Code Flow
 */
class HandleKeycloakCallbackUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(callbackUrl: String): Result<Pair<String, User>> {
        if (callbackUrl.isBlank()) {
            return Result.failure(IllegalArgumentException("Callback URL cannot be empty"))
        }

        return authRepository.handleKeycloakCallback(callbackUrl)
    }
}
