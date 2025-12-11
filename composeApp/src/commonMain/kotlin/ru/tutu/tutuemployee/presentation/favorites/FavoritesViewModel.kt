package ru.tutu.tutuemployee.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.tutu.tutuemployee.data.model.FavoriteCard
import ru.tutu.tutuemployee.data.network.ApiService

data class FavoritesUiState(
    val favorites: List<FavoriteCard> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val showAddDialog: Boolean = false
)

class FavoritesViewModel : ViewModel() {
    private val apiService = ApiService()

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            apiService.getFavorites()
                .onSuccess { favorites ->
                    _uiState.value = _uiState.value.copy(
                        favorites = favorites,
                        isLoading = false
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
        }
    }

    fun showAddDialog() {
        _uiState.value = _uiState.value.copy(showAddDialog = true)
    }

    fun hideAddDialog() {
        _uiState.value = _uiState.value.copy(showAddDialog = false)
    }

    fun addFavorite(title: String, url: String) {
        viewModelScope.launch {
            apiService.addFavorite(title, url)
                .onSuccess {
                    loadFavorites()
                    hideAddDialog()
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(error = error.message)
                }
        }
    }

    fun deleteFavorite(id: String) {
        viewModelScope.launch {
            apiService.deleteFavorite(id)
                .onSuccess {
                    loadFavorites()
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(error = error.message)
                }
        }
    }

    fun refresh() {
        loadFavorites()
    }
}
