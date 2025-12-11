package ru.tutu.tutuemployee.data.remote.dto

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

// Achievement DTO
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

// Vacation DTO
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

// FavoriteCard DTO
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
