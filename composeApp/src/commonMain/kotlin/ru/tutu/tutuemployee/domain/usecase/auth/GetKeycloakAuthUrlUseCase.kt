package ru.tutu.tutuemployee.domain.usecase.auth

import ru.tutu.tutuemployee.domain.repository.AuthRepository

/**
 * Use case для получения URL авторизации Keycloak
 * Используется для Authorization Code Flow с PKCE
 */
class GetKeycloakAuthUrlUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<String> {
        return authRepository.createKeycloakAuthUrl()
    }
}
