package ru.tutu.tutuemployee.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.tutu.tutuemployee.domain.model.*

// Birthday DTO
@Serializable
data class BirthdayDto(
    val employeeId: String,
    val employeeName: String,
    val date: String,
    val department: String,
    val avatarUrl: String? = null
)

fun BirthdayDto.toDomain() = Birthday(
    employeeId = employeeId,
    employeeName = employeeName,
    date = date,
    department = department,
    avatarUrl = avatarUrl
)

// Achievement DTO (согласно OpenAPI)
@Serializable
data class AchievementListResponse(
    @SerialName("user_id")
    val userId: String,
    @SerialName("total_points")
    val totalPoints: Int,
    val items: List<AchievementItemDto>
)

@Serializable
data class AchievementItemDto(
    val id: String,
    val title: String,
    val description: String,
    val points: Int,
    @SerialName("achieved_at")
    val achievedAt: String
)

@Serializable
data class AchievementDto(
    val id: String,
    val title: String,
    val description: String,
    val iconUrl: String? = null,
    val earnedAt: String
)

fun AchievementDto.toDomain() = Achievement(
    id = id,
    title = title,
    description = description,
    iconUrl = iconUrl,
    earnedAt = earnedAt
)

fun AchievementItemDto.toDomain() = Achievement(
    id = id,
    title = title,
    description = description,
    iconUrl = null,
    earnedAt = achievedAt
)

// Task DTO
@Serializable
data class TaskDto(
    val id: String,
    val title: String,
    val description: String,
    val status: TaskStatusDto,
    val dueDate: String? = null
)

@Serializable
enum class TaskStatusDto {
    TODO,
    IN_PROGRESS,
    DONE
}

fun TaskDto.toDomain() = Task(
    id = id,
    title = title,
    description = description,
    status = status.toDomain(),
    dueDate = dueDate
)

fun TaskStatusDto.toDomain() = when (this) {
    TaskStatusDto.TODO -> TaskStatus.TODO
    TaskStatusDto.IN_PROGRESS -> TaskStatus.IN_PROGRESS
    TaskStatusDto.DONE -> TaskStatus.DONE
}

// Vacation DTO (согласно OpenAPI)
@Serializable
data class VacationListResponse(
    @SerialName("employee_id")
    val employeeId: String,
    val items: List<VacationPeriodDto>
)

@Serializable
data class VacationPeriodDto(
    @SerialName("vacation_id")
    val vacationId: String,
    @SerialName("start_date")
    val startDate: String,
    @SerialName("end_date")
    val endDate: String,
    val type: String,
    @SerialName("approved_by")
    val approvedBy: String? = null,
    val status: String
)

@Serializable
data class VacationDto(
    val id: String,
    val startDate: String,
    val endDate: String,
    val daysCount: Int,
    val status: VacationStatusDto,
    val reason: String? = null
)

@Serializable
enum class VacationStatusDto {
    PLANNED,
    APPROVED,
    REJECTED
}

fun VacationDto.toDomain() = Vacation(
    id = id,
    startDate = startDate,
    endDate = endDate,
    daysCount = daysCount,
    status = status.toDomain(),
    reason = reason
)

fun VacationPeriodDto.toDomain(): Vacation {
    val status = when (status.lowercase()) {
        "approved" -> VacationStatus.APPROVED
        "pending" -> VacationStatus.PLANNED
        "rejected" -> VacationStatus.REJECTED
        else -> VacationStatus.PLANNED
    }
    return Vacation(
        id = vacationId,
        startDate = startDate,
        endDate = endDate,
        daysCount = 0, // Calculate if needed
        status = status,
        reason = type
    )
}

fun VacationStatusDto.toDomain() = when (this) {
    VacationStatusDto.PLANNED -> VacationStatus.PLANNED
    VacationStatusDto.APPROVED -> VacationStatus.APPROVED
    VacationStatusDto.REJECTED -> VacationStatus.REJECTED
}

// Course DTO
@Serializable
data class CourseDto(
    val id: String,
    val title: String,
    val description: String,
    val duration: String,
    val imageUrl: String? = null,
    val progress: Int = 0,
    val isCompleted: Boolean = false
)

fun CourseDto.toDomain() = Course(
    id = id,
    title = title,
    description = description,
    duration = duration,
    imageUrl = imageUrl,
    progress = progress,
    isCompleted = isCompleted
)

// WorkspaceBooking DTO
@Serializable
data class WorkspaceBookingDto(
    val id: String,
    val workspaceNumber: String,
    val date: String,
    val isBooked: Boolean,
    val bookedBy: String? = null,
    val floor: Int
)

fun WorkspaceBookingDto.toDomain() = WorkspaceBooking(
    id = id,
    workspaceNumber = workspaceNumber,
    date = date,
    isBooked = isBooked,
    bookedBy = bookedBy,
    floor = floor
)

// MerchItem DTO
@Serializable
data class MerchItemDto(
    val id: String,
    val name: String,
    val description: String,
    val price: Int,
    val imageUrl: String? = null,
    val category: MerchCategoryDto,
    val inStock: Boolean = true
)

@Serializable
enum class MerchCategoryDto {
    CLOTHING,
    ACCESSORIES,
    STATIONERY,
    ELECTRONICS
}

fun MerchItemDto.toDomain() = MerchItem(
    id = id,
    name = name,
    description = description,
    price = price,
    imageUrl = imageUrl,
    category = category.toDomain(),
    inStock = inStock
)

fun MerchCategoryDto.toDomain() = when (this) {
    MerchCategoryDto.CLOTHING -> MerchCategory.CLOTHING
    MerchCategoryDto.ACCESSORIES -> MerchCategory.ACCESSORIES
    MerchCategoryDto.STATIONERY -> MerchCategory.STATIONERY
    MerchCategoryDto.ELECTRONICS -> MerchCategory.ELECTRONICS
}

// FavoriteCard DTO (согласно OpenAPI)
@Serializable
data class FavoriteListResponse(
    @SerialName("user_id")
    val userId: String,
    val items: List<FavoriteItemDto>
)

@Serializable
data class FavoriteItemDto(
    val id: String,
    val link: String,
    val description: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null
)

@Serializable
data class AddFavoriteRequest(
    @SerialName("user_id")
    val userId: String,
    val link: String,
    val description: String? = null
)

@Serializable
data class FavoriteCardDto(
    val id: String,
    val title: String,
    val url: String,
    val iconUrl: String? = null
)

fun FavoriteCardDto.toDomain() = FavoriteCard(
    id = id,
    title = title,
    url = url,
    iconUrl = iconUrl
)

fun FavoriteItemDto.toDomain() = FavoriteCard(
    id = id,
    title = description ?: link,
    url = link,
    iconUrl = null
)

// Auth DTO
@Serializable
data class AuthRequest(
    val username: String,
    val password: String
)

@Serializable
data class AuthResponse(
    val token: String,
    val user: UserDto
)

// Error Response (согласно OpenAPI)
@Serializable
data class ErrorResponse(
    val error: String,
    val message: String
)
