package ru.tutu.tutuemployee.presentation.office

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import ru.tutu.tutuemployee.domain.model.News
import ru.tutu.tutuemployee.domain.model.WorkspaceBooking
import ru.tutu.tutuemployee.domain.repository.NewsRepository
import ru.tutu.tutuemployee.domain.repository.OfficeRepository

data class OfficeUiState(
    val workspaceBookings: List<WorkspaceBooking> = emptyList(),
    val officeNews: List<News> = emptyList(),
    val selectedDate: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class OfficeViewModel(
    private val officeRepository: OfficeRepository,
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OfficeUiState())
    val uiState: StateFlow<OfficeUiState> = _uiState.asStateFlow()

    init {
        // Используем текущую дату
        val today = "2024-12-11" // TODO: использовать реальную текущую дату
        _uiState.value = _uiState.value.copy(selectedDate = today)
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val workspacesResult =
                officeRepository.getWorkspaceBookings(_uiState.value.selectedDate)
            val newsResult = newsRepository.getOfficeNews()

            _uiState.value = _uiState.value.copy(
                workspaceBookings = workspacesResult.getOrDefault(emptyList()),
                officeNews = newsResult.getOrDefault(emptyList()),
                isLoading = false
            )
        }
    }

    fun selectDate(date: String) {
        _uiState.value = _uiState.value.copy(selectedDate = date)
        loadData()
    }

    fun refresh() {
        loadData()
    }
}
