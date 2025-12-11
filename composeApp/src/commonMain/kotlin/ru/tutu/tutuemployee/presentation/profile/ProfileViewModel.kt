package ru.tutu.tutuemployee.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.tutu.tutuemployee.data.model.*
import ru.tutu.tutuemployee.data.network.ApiService

data class ProfileUiState(
    val user: User? = null,
    val achievements: List<Achievement> = emptyList(),
    val tasks: List<Task> = emptyList(),
    val vacations: List<Vacation> = emptyList(),
    val courses: List<Course> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class ProfileViewModel : ViewModel() {
    private val apiService = ApiService()

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val userResult = apiService.getCurrentUser()
            val achievementsResult = apiService.getAchievements()
            val tasksResult = apiService.getTasks()
            val vacationsResult = apiService.getVacations()
            val coursesResult = apiService.getCourses()

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

    fun refresh() {
        loadData()
    }
}
