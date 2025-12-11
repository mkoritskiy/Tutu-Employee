package ru.tutu.tutuemployee.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.tutu.tutuemployee.domain.model.Birthday
import ru.tutu.tutuemployee.domain.model.News
import ru.tutu.tutuemployee.domain.model.User
import ru.tutu.tutuemployee.domain.usecase.employee.GetBirthdaysUseCase
import ru.tutu.tutuemployee.domain.usecase.employee.SearchEmployeesUseCase
import ru.tutu.tutuemployee.domain.usecase.news.GetNewsUseCase

data class HomeUiState(
    val news: List<News> = emptyList(),
    val birthdays: List<Birthday> = emptyList(),
    val searchResults: List<User> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class HomeViewModel(
    private val getNewsUseCase: GetNewsUseCase,
    private val getBirthdaysUseCase: GetBirthdaysUseCase,
    private val searchEmployeesUseCase: SearchEmployeesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val newsResult = getNewsUseCase()
            val birthdaysResult = getBirthdaysUseCase()

            _uiState.value = _uiState.value.copy(
                news = newsResult.getOrDefault(emptyList()),
                birthdays = birthdaysResult.getOrDefault(emptyList()),
                isLoading = false
            )
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)

        if (query.length >= 2) {
            searchEmployees(query)
        } else {
            _uiState.value = _uiState.value.copy(searchResults = emptyList())
        }
    }

    private fun searchEmployees(query: String) {
        viewModelScope.launch {
            searchEmployeesUseCase(query)
                .onSuccess { results ->
                    _uiState.value = _uiState.value.copy(searchResults = results)
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(searchResults = emptyList())
                }
        }
    }

    fun refresh() {
        loadData()
    }
}
