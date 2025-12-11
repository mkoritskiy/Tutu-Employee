package ru.tutu.tutuemployee.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.tutu.tutuemployee.domain.model.Achievement
import ru.tutu.tutuemployee.domain.model.Course
import ru.tutu.tutuemployee.domain.model.Task
import ru.tutu.tutuemployee.domain.model.User
import ru.tutu.tutuemployee.domain.model.Vacation
import ru.tutu.tutuemployee.domain.usecase.auth.GetCurrentUserUseCase
import ru.tutu.tutuemployee.domain.usecase.profile.GetAchievementsUseCase
import ru.tutu.tutuemployee.domain.usecase.profile.GetCoursesUseCase
import ru.tutu.tutuemployee.domain.usecase.profile.GetTasksUseCase
import ru.tutu.tutuemployee.domain.usecase.profile.GetVacationsUseCase

data class ProfileUiState(
    val user: User? = null,
    val achievements: List<Achievement> = emptyList(),
    val tasks: List<Task> = emptyList(),
    val vacations: List<Vacation> = emptyList(),
    val courses: List<Course> = emptyList(),
    val selectedTab: ProfileTab = ProfileTab.ACHIEVEMENTS,
    val isLoading: Boolean = false,
    val error: String? = null
)

enum class ProfileTab {
    ACHIEVEMENTS,
    TASKS,
    VACATIONS,
    COURSES
}

class ProfileViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getAchievementsUseCase: GetAchievementsUseCase,
    private val getTasksUseCase: GetTasksUseCase,
    private val getVacationsUseCase: GetVacationsUseCase,
    private val getCoursesUseCase: GetCoursesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val userResult = getCurrentUserUseCase()
            val achievementsResult = getAchievementsUseCase()
            val tasksResult = getTasksUseCase()
            val vacationsResult = getVacationsUseCase()
            val coursesResult = getCoursesUseCase()

            _uiState.value = _uiState.value.copy(
                user = userResult.getOrNull(),
                achievements = achievementsResult.getOrDefault(emptyList()),
                tasks = tasksResult.getOrDefault(emptyList()),
                vacations = vacationsResult.getOrDefault(emptyList()),
                courses = coursesResult.getOrDefault(emptyList()),
                isLoading = false
            )
        }
    }

    fun selectTab(tab: ProfileTab) {
        _uiState.value = _uiState.value.copy(selectedTab = tab)
    }

    fun refresh() {
        loadData()
    }
}
