package ru.tutu.tutuemployee.domain.repository

import ru.tutu.tutuemployee.domain.model.Birthday
import ru.tutu.tutuemployee.domain.model.User

/**
 * Repository interface для работы с сотрудниками
 */
interface EmployeeRepository {
    suspend fun searchEmployees(query: String): Result<List<User>>
    suspend fun getBirthdays(): Result<List<Birthday>>
}
