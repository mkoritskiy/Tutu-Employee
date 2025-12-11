package ru.tutu.tutuemployee.data.remote.dto

import kotlinx.serialization.Serializable
import ru.tutu.tutuemployee.domain.model.User

/**
 * DTO для пользователя из API
 */
@Serializable
data class UserDto(
    val id: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val position: String,
    val department: String,
    val legalEntity: String,
    val email: String,
    val avatarUrl: String? = null,
    val availableVacationDays: Int = 0,
    val bonusPoints: Int = 0
)

/**
 * Маппер DTO -> Domain
 */
fun UserDto.toDomain(): User {
    return User(
        id = id,
        username = username,
        firstName = firstName,
        lastName = lastName,
        position = position,
        department = department,
        legalEntity = legalEntity,
        email = email,
        avatarUrl = avatarUrl,
        availableVacationDays = availableVacationDays,
        bonusPoints = bonusPoints
    )
}
