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

    override suspend fun getAchievements(): Result<List<AchievementDto>> {
        return apiService.getAchievements()
    }

    override suspend fun getTasks(): Result<List<TaskDto>> {
        return apiService.getTasks()
    }

    override suspend fun getVacations(): Result<List<VacationDto>> {
        return apiService.getVacations()
    }

    override suspend fun getCourses(): Result<List<CourseDto>> {
        return apiService.getCourses()
    }
}
