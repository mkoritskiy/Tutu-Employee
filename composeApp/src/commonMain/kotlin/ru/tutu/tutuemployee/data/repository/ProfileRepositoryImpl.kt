package ru.tutu.tutuemployee.data.repository

import ru.tutu.tutuemployee.data.remote.datasource.ProfileRemoteDataSource
import ru.tutu.tutuemployee.data.remote.dto.toDomain
import ru.tutu.tutuemployee.domain.model.Achievement
import ru.tutu.tutuemployee.domain.model.Course
import ru.tutu.tutuemployee.domain.model.Task
import ru.tutu.tutuemployee.domain.model.Vacation
import ru.tutu.tutuemployee.domain.repository.ProfileRepository

/**
 * Реализация ProfileRepository
 */
class ProfileRepositoryImpl(
    private val remoteDataSource: ProfileRemoteDataSource
) : ProfileRepository {

    override suspend fun getAchievements(): Result<List<Achievement>> {
        return remoteDataSource.getAchievements()
            .map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getTasks(): Result<List<Task>> {
        return remoteDataSource.getTasks()
            .map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getVacations(): Result<List<Vacation>> {
        return remoteDataSource.getVacations()
            .map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getCourses(): Result<List<Course>> {
        return remoteDataSource.getCourses()
            .map { list -> list.map { it.toDomain() } }
    }
}
