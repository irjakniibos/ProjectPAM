package com.example.pengelolaandatamotorshowroom.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pengelolaandatamotorshowroom.modeldata.DataMotor
import com.example.pengelolaandatamotorshowroom.repositori.RepositoryMotor
import kotlinx.coroutines.launch

class DetailMotorViewModel(private val repositoryMotor: RepositoryMotor) : ViewModel() {

    var uiState by mutableStateOf(DetailMotorUiState())
        private set

    fun loadMotorDetail(motorId: Int) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            try {
                val motor = repositoryMotor.getMotorDetail(motorId)
                uiState = uiState.copy(
                    motor = motor,
                    isLoading = false
                )
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }
}

data class DetailMotorUiState(
    val motor: DataMotor? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)