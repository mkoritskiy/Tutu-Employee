package ru.tutu.tutuemployee.data.repository

import ru.tutu.tutuemployee.data.remote.datasource.EmployeeRemoteDataSource
import ru.tutu.tutuemployee.data.remote.dto.toDomain
import ru.tutu.tutuemployee.domain.model.Birthday
import ru.tutu.tutuemployee.domain.model.User
import ru.tutu.tutuemployee.domain.repository.EmployeeRepository

/**
 * Реализация EmployeeRepository
 */
class EmployeeRepositoryImpl(
    private val remoteDataSource: EmployeeRemoteDataSource
) : EmployeeRepository {

    override suspend fun searchEmployees(query: String): Result<List<User>> {
        return remoteDataSource.searchEmployees(query)
            .map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getBirthdays(): Result<List<Birthday>> {
        return remoteDataSource.getBirthdays()
            .map { list -> list.map { it.toDomain() } }
    }
}
