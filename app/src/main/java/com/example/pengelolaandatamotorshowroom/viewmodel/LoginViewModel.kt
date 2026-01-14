package com.example.pengelolaandatamotorshowroom.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pengelolaandatamotorshowroom.repositori.RepositoryAuth
import kotlinx.coroutines.launch

class LoginViewModel(private val repositoryAuth: RepositoryAuth) : ViewModel() {

    var uiState by mutableStateOf(LoginUiState())
        private set

    fun updateEmail(email: String) {
        uiState = uiState.copy(email = email, emailError = null)
    }

    fun updatePassword(password: String) {
        uiState = uiState.copy(password = password, passwordError = null)
    }

    fun login(onSuccess: () -> Unit) {
        // Validasi
        if (uiState.email.isBlank()) {
            uiState = uiState.copy(emailError = "Email tidak boleh kosong")
            return
        }
        if (uiState.password.isBlank()) {
            uiState = uiState.copy(passwordError = "Password tidak boleh kosong")
            return
        }

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            try {
                val response = repositoryAuth.login(uiState.email, uiState.password)
                if (response.success) {
                    uiState = uiState.copy(isLoading = false)
                    onSuccess()
                } else {
                    uiState = uiState.copy(
                        isLoading = false,
                        errorMessage = response.message
                    )
                }
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Terjadi kesalahan"
                )
            }
        }
    }
}

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)