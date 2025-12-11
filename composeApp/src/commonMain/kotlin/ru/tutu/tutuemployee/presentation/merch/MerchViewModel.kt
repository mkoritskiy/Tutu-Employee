package ru.tutu.tutuemployee.presentation.merch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.tutu.tutuemployee.domain.model.MerchCategory
import ru.tutu.tutuemployee.domain.model.MerchItem
import ru.tutu.tutuemployee.domain.repository.MerchRepository

data class MerchUiState(
    val items: List<MerchItem> = emptyList(),
    val filteredItems: List<MerchItem> = emptyList(),
    val selectedCategory: MerchCategory? = null,
    val userPoints: Int = 1000, // Заглушка, в будущем загрузить из профиля
    val isLoading: Boolean = false,
    val error: String? = null,
    val purchaseSuccess: String? = null
)

class MerchViewModel(
    private val merchRepository: MerchRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MerchUiState())
    val uiState: StateFlow<MerchUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            merchRepository.getMerchItems()
                .onSuccess { items ->
                    _uiState.value = _uiState.value.copy(
                        items = items,
                        filteredItems = items,
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

    fun selectCategory(category: MerchCategory?) {
        val items = _uiState.value.items
        val filtered = if (category == null) {
            items
        } else {
            items.filter { it.category == category }
        }

        _uiState.value = _uiState.value.copy(
            selectedCategory = category,
            filteredItems = filtered
        )
    }

    fun purchaseItem(itemId: String) {
        viewModelScope.launch {
            val item = _uiState.value.items.find { it.id == itemId }
            val currentPoints = _uiState.value.userPoints

            if (item == null) {
                _uiState.value = _uiState.value.copy(error = "Товар не найден")
                return@launch
            }

            if (currentPoints < item.price) {
                _uiState.value = _uiState.value.copy(error = "Недостаточно баллов")
                return@launch
            }

            merchRepository.purchaseMerchItem(itemId)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        purchaseSuccess = "Покупка успешна! -${item.price} баллов",
                        userPoints = currentPoints - item.price
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(error = error.message)
                }
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            purchaseSuccess = null,
            error = null
        )
    }

    fun refresh() {
        loadData()
    }
}
