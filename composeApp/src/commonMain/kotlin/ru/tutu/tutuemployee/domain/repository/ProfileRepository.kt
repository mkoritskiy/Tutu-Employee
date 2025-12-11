package ru.tutu.tutuemployee.domain.repository

import ru.tutu.tutuemployee.domain.model.Achievement
import ru.tutu.tutuemployee.domain.model.Course
import ru.tutu.tutuemployee.domain.model.Task
import ru.tutu.tutuemployee.domain.model.Vacation

/**
 * Repository interface для профиля пользователя
 */
interface ProfileRepository {
    suspend fun getAchievements(): Result<List<Achievement>>
    suspend fun getTasks(): Result<List<Task>>
    suspend fun getVacations(): Result<List<Vacation>>
    suspend fun getCourses(): Result<List<Course>>
}
