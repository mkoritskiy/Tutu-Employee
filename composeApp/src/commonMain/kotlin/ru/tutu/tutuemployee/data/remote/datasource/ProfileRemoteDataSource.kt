package ru.tutu.tutuemployee.data.remote.datasource

import ru.tutu.tutuemployee.data.remote.api.ApiService
import ru.tutu.tutuemployee.data.remote.dto.*

/**
 * Remote data source для работы с профилем пользователя
 */
interface ProfileRemoteDataSource {
    suspend fun getAchievements(): Result<List<AchievementDto>>
    suspend fun getTasks(): Result<List<TaskDto>>
    suspend fun getVacations(): Result<List<VacationDto>>
    suspend fun getCourses(): Result<List<CourseDto>>
}

class ProfileRemoteDataSourceImpl(
    private val apiService: ApiService
) : ProfileRemoteDataSource {

    // TODO: получать userId из AuthManager когда он будет реализован
    private fun getCurrentUserId(): String = "user-123"

    override suspend fun getAchievements(): Result<List<AchievementDto>> {
        val userId = getCurrentUserId()
        return apiService.getAchievements(userId).map { response ->
            response.items.map { item ->
                AchievementDto(
                    id = item.id,
                    title = item.title,
                    description = item.description,
                    iconUrl = null,
                    earnedAt = item.achievedAt
                )
            }
        }
    }

    override suspend fun getTasks(): Result<List<TaskDto>> {
        return apiService.getTasks()
    }

    override suspend fun getVacations(): Result<List<VacationDto>> {
        // Используем новый API для получения отпусков
        val userId = getCurrentUserId()
        return apiService.getVacations(userId).map { response ->
            response.items.map { period ->
                VacationDto(
                    id = period.vacationId,
                    startDate = period.startDate,
                    endDate = period.endDate,
                    daysCount = calculateDays(period.startDate, period.endDate),
                    status = mapVacationStatus(period.status),
                    reason = period.type
                )
            }
        }
    }

    override suspend fun getCourses(): Result<List<CourseDto>> {
        return apiService.getCourses()
    }

    private fun calculateDays(startDate: String, endDate: String): Int {
        // Simple calculation - можно улучшить
        return 14 // Default value
    }

    private fun mapVacationStatus(status: String): VacationStatusDto {
        return when (status.lowercase()) {
            "approved" -> VacationStatusDto.APPROVED
            "pending" -> VacationStatusDto.PLANNED
            "rejected" -> VacationStatusDto.REJECTED
            else -> VacationStatusDto.PLANNED
        }
    }
}
