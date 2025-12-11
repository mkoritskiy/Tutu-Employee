package ru.tutu.tutuemployee.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.tutu.tutuemployee.domain.model.User
import ru.tutu.tutuemployee.domain.usecase.auth.GetKeycloakAuthUrlUseCase
import ru.tutu.tutuemployee.domain.usecase.auth.HandleKeycloakCallbackUseCase
import ru.tutu.tutuemployee.domain.usecase.auth.LoginUseCase

data class AuthUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isAuthenticated: Boolean = false,
    val user: User? = null,
    val keycloakAuthUrl: String? = null
)

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val getKeycloakAuthUrlUseCase: GetKeycloakAuthUrlUseCase,
    private val handleKeycloakCallbackUseCase: HandleKeycloakCallbackUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onUsernameChange(username: String) {
        _uiState.value = _uiState.value.copy(username = username, error = null)
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password, error = null)
    }

    /**
     * Обычный вход (legacy)
     */
    fun login() {
        val state = _uiState.value
        if (state.username.isBlank() || state.password.isBlank()) {
            _uiState.value = state.copy(error = "Please fill in all fields")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            loginUseCase(state.username, state.password)
                .onSuccess { (token, user) ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isAuthenticated = true,
                        user = user,
                        error = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Login failed"
                    )
                }
        }
    }

    /**
     * Получить URL для OAuth авторизации через Keycloak
     */
    fun startKeycloakOAuth() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            getKeycloakAuthUrlUseCase()
                .onSuccess { url ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        keycloakAuthUrl = url,
                        error = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Ошибка инициализации OAuth"
                    )
                }
        }
    }

    /**
     * Обработать callback от Keycloak
     */
    fun handleKeycloakCallback(callbackUrl: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            handleKeycloakCallbackUseCase(callbackUrl)
                .onSuccess { (token, user) ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isAuthenticated = true,
                        user = user,
                        keycloakAuthUrl = null,
                        error = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Ошибка авторизации"
                    )
                }
        }
    }

    fun clearKeycloakAuthUrl() {
        _uiState.value = _uiState.value.copy(keycloakAuthUrl = null)
    }
}
