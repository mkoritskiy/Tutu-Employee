package ru.tutu.tutuemployee.domain.usecase.employee

import ru.tutu.tutuemployee.domain.model.Birthday
import ru.tutu.tutuemployee.domain.repository.EmployeeRepository

/**
 * Use case для получения дней рождений
 */
class GetBirthdaysUseCase(
    private val employeeRepository: EmployeeRepository
) {
    suspend operator fun invoke(): Result<List<Birthday>> {
        return employeeRepository.getBirthdays()
    }
}
