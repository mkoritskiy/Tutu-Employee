package ru.tutu.tutuemployee.domain.common

/**
 * Sealed class для представления результата операции
 */
sealed class DomainResult<out T> {
    data class Success<T>(val data: T) : DomainResult<T>()
    data class Error(val exception: DomainException) : DomainResult<Nothing>()
    data object Loading : DomainResult<Nothing>()
}

/**
 * Extension функции для работы с Result
 */
inline fun <T> DomainResult<T>.onSuccess(action: (T) -> Unit): DomainResult<T> {
    if (this is DomainResult.Success) {
        action(data)
    }
    return this
}

inline fun <T> DomainResult<T>.onError(action: (DomainException) -> Unit): DomainResult<T> {
    if (this is DomainResult.Error) {
        action(exception)
    }
    return this
}

inline fun <T, R> DomainResult<T>.map(transform: (T) -> R): DomainResult<R> {
    return when (this) {
        is DomainResult.Success -> DomainResult.Success(transform(data))
        is DomainResult.Error -> DomainResult.Error(exception)
        DomainResult.Loading -> DomainResult.Loading
    }
}

fun <T> DomainResult<T>.getOrNull(): T? {
    return when (this) {
        is DomainResult.Success -> data
        else -> null
    }
}

fun <T> DomainResult<T>.getOrThrow(): T {
    return when (this) {
        is DomainResult.Success -> data
        is DomainResult.Error -> throw exception
        DomainResult.Loading -> throw IllegalStateException("Result is still loading")
    }
}
