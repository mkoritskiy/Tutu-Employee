package ru.tutu.tutuemployee.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.tutu.tutuemployee.domain.model.User

/**
 * DTO для пользователя из API (согласно OpenAPI спецификации)
 */
@Serializable
data class UserDto(
    val id: String,
    val status: String? = null,
    val personal: PersonalInfo? = null,
    val employment: EmploymentInfo? = null,
    val roles: List<String> = emptyList(),
    val manager: ManagerInfo? = null,
    @SerialName("external_ids")
    val externalIds: Map<String, String> = emptyMap()
)

@Serializable
data class PersonalInfo(
    @SerialName("first_name")
    val firstName: String,
    @SerialName("last_name")
    val lastName: String,
    @SerialName("middle_name")
    val middleName: String? = null,
    val email: String,
    val phone: String? = null
)

@Serializable
data class EmploymentInfo(
    val position: String,
    val department: String,
    val location: String? = null,
    @SerialName("start_date")
    val startDate: String? = null
)

@Serializable
data class ManagerInfo(
    val id: String,
    val name: String,
    val email: String
)

/**
 * Маппер DTO -> Domain
 */
fun UserDto.toDomain(): User {
    return User(
        id = id,
        username = personal?.email?.substringBefore("@") ?: "",
        firstName = personal?.firstName ?: "",
        lastName = personal?.lastName ?: "",
        position = employment?.position ?: "",
        department = employment?.department ?: "",
        legalEntity = employment?.location ?: "",
        email = personal?.email ?: "",
        avatarUrl = null,
        availableVacationDays = 0,
        bonusPoints = 0
    )
}

/**
 * DTO для поиска сотрудников (preview)
 */
@Serializable
data class EmployeePreviewDto(
    val id: String,
    @SerialName("first_name")
    val firstName: String,
    @SerialName("last_name")
    val lastName: String,
    val department: String,
    val position: String,
    @SerialName("avatar_url")
    val avatarUrl: String? = null
)

fun EmployeePreviewDto.toUserDto(): UserDto {
    return UserDto(
        id = id,
        personal = PersonalInfo(
            firstName = firstName,
            lastName = lastName,
            email = ""
        ),
        employment = EmploymentInfo(
            position = position,
            department = department
        )
    )
}

/**
 * Response для поиска сотрудников
 */
@Serializable
data class EmployeeSearchResponse(
    val total: Int,
    val items: List<EmployeePreviewDto>
)
