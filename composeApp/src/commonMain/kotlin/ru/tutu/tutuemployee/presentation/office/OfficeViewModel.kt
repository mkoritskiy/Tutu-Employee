package ru.tutu.tutuemployee.presentation.office

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.tutu.tutuemployee.data.model.News
import ru.tutu.tutuemployee.data.model.WorkspaceBooking
import ru.tutu.tutuemployee.data.network.ApiService

data class OfficeUiState(
    val workspaceBookings: List<WorkspaceBooking> = emptyList(),
    val officeNews: List<News> = emptyList(),
    val selectedDate: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class OfficeViewModel : ViewModel() {
    private val apiService = ApiService()

    private val _uiState = MutableStateFlow(OfficeUiState())
    val uiState: StateFlow<OfficeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val today = getCurrentDate()
            val workspacesResult = apiService.getWorkspaceBookings(today)
            val newsResult = apiService.getOfficeNews()

            _uiState.value = _uiState.value.copy(
                workspaceBookings = workspacesResult.getOrDefault(emptyList()),
                officeNews = newsResult.getOrDefault(emptyList()),
                selectedDate = today,
                isLoading = false
            )
        }
    }

    fun selectDate(date: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, selectedDate = date)

            apiService.getWorkspaceBookings(date)
                .onSuccess { bookings ->
                    _uiState.value = _uiState.value.copy(
                        workspaceBookings = bookings,
                        isLoading = false
                    )
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
        }
    }

    fun refresh() {
        loadData()
    }

    private fun getCurrentDate(): String {
        // Здесь используется временная реализация
        // В реальном приложении используйте kotlinx.datetime
        return "2024-01-15"
    }
}
