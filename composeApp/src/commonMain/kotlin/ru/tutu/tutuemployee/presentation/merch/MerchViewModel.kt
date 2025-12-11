package ru.tutu.tutuemployee.presentation.merch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.tutu.tutuemployee.data.model.MerchCategory
import ru.tutu.tutuemployee.data.model.MerchItem
import ru.tutu.tutuemployee.data.network.ApiService

data class MerchUiState(
    val items: List<MerchItem> = emptyList(),
    val selectedCategory: MerchCategory? = null,
    val userPoints: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val purchaseSuccess: String? = null
)

class MerchViewModel : ViewModel() {
    private val apiService = ApiService()

    private val _uiState = MutableStateFlow(MerchUiState())
    val uiState: StateFlow<MerchUiState> = _uiState.asStateFlow()

    init {
        loadMerchItems()
        loadUserPoints()
    }

    private fun loadMerchItems() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            apiService.getMerchItems()
                .onSuccess { items ->
                    _uiState.value = _uiState.value.copy(
                        items = items,
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

    private fun loadUserPoints() {
        viewModelScope.launch {
            apiService.getCurrentUser()
                .onSuccess { user ->
                    _uiState.value = _uiState.value.copy(userPoints = user.bonusPoints)
                }
        }
    }

    fun selectCategory(category: MerchCategory?) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun purchaseItem(itemId: String) {
        viewModelScope.launch {
            apiService.purchaseMerchItem(itemId)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        purchaseSuccess = "Товар успешно приобретен!",
                        error = null
                    )
                    loadMerchItems()
                    loadUserPoints()
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        error = error.message ?: "Ошибка покупки",
                        purchaseSuccess = null
                    )
                }
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            error = null,
            purchaseSuccess = null
        )
    }

    fun refresh() {
        loadMerchItems()
        loadUserPoints()
    }
}
