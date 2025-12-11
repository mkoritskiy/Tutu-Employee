package ru.tutu.tutuemployee.domain.usecase.employee

import ru.tutu.tutuemployee.domain.model.User
import ru.tutu.tutuemployee.domain.repository.EmployeeRepository

/**
 * Use case для поиска сотрудников
 */
class SearchEmployeesUseCase(
    private val employeeRepository: EmployeeRepository
) {
    suspend operator fun invoke(query: String): Result<List<User>> {
        if (query.length < 2) {
            return Result.success(emptyList())
        }

        return employeeRepository.searchEmployees(query)
    }
}
