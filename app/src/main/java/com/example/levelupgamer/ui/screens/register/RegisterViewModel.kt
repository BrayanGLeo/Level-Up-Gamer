package com.example.levelupgamer.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupgamer.data.model.User
import com.example.levelupgamer.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class RegisterUiState(
    val isLoading: Boolean = false,
    val registerSuccessUser: User? = null,
    val error: String? = null
)

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    fun register(email: String, name: String, password: String, confirmPass: String) {
        if (password != confirmPass) {
            _uiState.value = RegisterUiState(error = "Las contraseñas no coinciden")
            return
        }

        viewModelScope.launch {
            _uiState.value = RegisterUiState(isLoading = true)
            val result = authRepository.register(email, name, password)
            result.fold(
                onSuccess = { user ->
                    _uiState.value = RegisterUiState(registerSuccessUser = user)
                },
                onFailure = {
                    _uiState.value = RegisterUiState(error = it.message)
                }
            )
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}