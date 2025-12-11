package ru.tutu.tutuemployee.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.tutu.tutuemployee.data.model.Birthday
import ru.tutu.tutuemployee.data.model.News
import ru.tutu.tutuemployee.data.model.User
import ru.tutu.tutuemployee.data.network.ApiService

data class HomeUiState(
    val news: List<News> = emptyList(),
    val birthdays: List<Birthday> = emptyList(),
    val searchResults: List<User> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class HomeViewModel : ViewModel() {
    private val apiService = ApiService()

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val newsResult = apiService.getNews()
            val birthdaysResult = apiService.getBirthdays()

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
            apiService.searchEmployees(query)
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
