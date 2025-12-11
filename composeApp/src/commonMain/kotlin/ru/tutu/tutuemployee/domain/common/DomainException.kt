package ru.tutu.tutuemployee.domain.common

/**
 * Базовый класс для всех исключений в domain слое
 */
sealed class DomainException(
    override val message: String,
    override val cause: Throwable? = null
) : Exception(message, cause) {

    /**
     * Ошибка сети
     */
    data class NetworkException(
        override val message: String = "Network error occurred",
        override val cause: Throwable? = null
    ) : DomainException(message, cause)

    /**
     * Ошибка авторизации
     */
    data class AuthenticationException(
        override val message: String = "Authentication failed",
        override val cause: Throwable? = null
    ) : DomainException(message, cause)

    /**
     * Ошибка доступа
     */
    data class AuthorizationException(
        override val message: String = "Access denied",
        override val cause: Throwable? = null
    ) : DomainException(message, cause)

    /**
     * Ошибка валидации
     */
    data class ValidationException(
        override val message: String,
        override val cause: Throwable? = null
    ) : DomainException(message, cause)

    /**
     * Ресурс не найден
     */
    data class NotFoundException(
        override val message: String = "Resource not found",
        override val cause: Throwable? = null
    ) : DomainException(message, cause)

    /**
     * Ошибка сервера
     */
    data class ServerException(
        override val message: String = "Server error occurred",
        override val cause: Throwable? = null
    ) : DomainException(message, cause)

    /**
     * Неизвестная ошибка
     */
    data class UnknownException(
        override val message: String = "Unknown error occurred",
        override val cause: Throwable? = null
    ) : DomainException(message, cause)
}

/**
 * Extension для конвертации Throwable в DomainException
 */
fun Throwable.toDomainException(): DomainException {
    return when (this) {
        is DomainException -> this
        else -> DomainException.UnknownException(
            message = this.message ?: "Unknown error",
            cause = this
        )
    }
}
