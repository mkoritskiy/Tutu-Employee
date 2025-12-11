package ru.tutu.tutuemployee.domain.usecase.auth

import ru.tutu.tutuemployee.domain.model.User
import ru.tutu.tutuemployee.domain.repository.AuthRepository

/**
 * Use case для получения текущего пользователя
 */
class GetCurrentUserUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<User> {
        return authRepository.getCurrentUser()
    }
}
