package ru.tutu.tutuemployee.domain.model

/**
 * Domain model для пользователя
 */
data class User(
    val id: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val position: String,
    val department: String,
    val legalEntity: String,
    val email: String,
    val avatarUrl: String?,
    val availableVacationDays: Int,
    val bonusPoints: Int
) {
    val fullName: String
        get() = "$firstName $lastName"
}
