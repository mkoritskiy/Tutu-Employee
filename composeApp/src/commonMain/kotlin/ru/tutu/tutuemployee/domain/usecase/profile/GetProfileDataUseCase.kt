package ru.tutu.tutuemployee.domain.usecase.profile

import ru.tutu.tutuemployee.domain.model.Achievement
import ru.tutu.tutuemployee.domain.model.Course
import ru.tutu.tutuemployee.domain.model.Task
import ru.tutu.tutuemployee.domain.model.Vacation
import ru.tutu.tutuemployee.domain.repository.ProfileRepository

/**
 * Use case для получения данных профиля
 */
class GetAchievementsUseCase(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(): Result<List<Achievement>> {
        return profileRepository.getAchievements()
    }
}

class GetTasksUseCase(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(): Result<List<Task>> {
        return profileRepository.getTasks()
    }
}

class GetVacationsUseCase(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(): Result<List<Vacation>> {
        return profileRepository.getVacations()
    }
}

class GetCoursesUseCase(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(): Result<List<Course>> {
        return profileRepository.getCourses()
    }
}
