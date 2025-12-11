package ru.tutu.tutuemployee.data.remote.datasource

import ru.tutu.tutuemployee.data.remote.api.ApiService
import ru.tutu.tutuemployee.data.remote.dto.BirthdayDto
import ru.tutu.tutuemployee.data.remote.dto.UserDto

/**
 * Remote data source для работы с сотрудниками
 */
interface EmployeeRemoteDataSource {
    suspend fun searchEmployees(query: String): Result<List<UserDto>>
    suspend fun getBirthdays(): Result<List<BirthdayDto>>
}

class EmployeeRemoteDataSourceImpl(
    private val apiService: ApiService
) : EmployeeRemoteDataSource {

    override suspend fun searchEmployees(query: String): Result<List<UserDto>> {
        return apiService.searchEmployees(query)
    }

    override suspend fun getBirthdays(): Result<List<BirthdayDto>> {
        return apiService.getBirthdays()
    }
}
