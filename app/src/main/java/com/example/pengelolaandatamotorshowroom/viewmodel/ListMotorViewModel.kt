package com.example.pengelolaandatamotorshowroom.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pengelolaandatamotorshowroom.modeldata.DataMotor
import com.example.pengelolaandatamotorshowroom.repositori.RepositoryMotor
import kotlinx.coroutines.launch

class ListMotorViewModel(private val repositoryMotor: RepositoryMotor) : ViewModel() {

    var uiState by mutableStateOf(ListMotorUiState())
        private set

    fun loadMotors(brandId: Int, brandName: String) {
        uiState = uiState.copy(brandId = brandId, brandName = brandName)
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            try {
                val motors = repositoryMotor.getMotorsByBrand(brandId)
                uiState = uiState.copy(
                    motors = motors,
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

    fun showDeleteDialog(motor: DataMotor) {
        uiState = uiState.copy(showDeleteDialog = true, motorToDelete = motor)
    }

    fun hideDeleteDialog() {
        uiState = uiState.copy(showDeleteDialog = false, motorToDelete = null)
    }

    fun deleteMotor() {
        val motor = uiState.motorToDelete ?: return
        viewModelScope.launch {
            try {
                repositoryMotor.deleteMotor(motor.id)
                uiState = uiState.copy(successMessage = "Motor berhasil dihapus")
                hideDeleteDialog()
                loadMotors(uiState.brandId, uiState.brandName)
            } catch (e: Exception) {
                uiState = uiState.copy(errorMessage = e.message)
                hideDeleteDialog()
            }
        }
    }

    fun showStokDialog(motor: DataMotor, isAdd: Boolean) {
        uiState = uiState.copy(
            showStokDialog = true,
            motorForStok = motor,
            isAddStok = isAdd
        )
    }

    fun hideStokDialog() {
        uiState = uiState.copy(showStokDialog = false, motorForStok = null)
    }

    fun updateStok() {
        val motor = uiState.motorForStok ?: return
        viewModelScope.launch {
            try {
                if (uiState.isAddStok) {
                    repositoryMotor.tambahStok(motor.id)
                    uiState = uiState.copy(successMessage = "Stok berhasil ditambah")
                } else {
                    repositoryMotor.kurangiStok(motor.id)
                    uiState = uiState.copy(successMessage = "Stok berhasil dikurangi")
                }
                hideStokDialog()
                loadMotors(uiState.brandId, uiState.brandName)
            } catch (e: Exception) {
                uiState = uiState.copy(errorMessage = e.message)
                hideStokDialog()
            }
        }
    }

    fun clearSuccessMessage() {
        uiState = uiState.copy(successMessage = null)
    }
}

data class ListMotorUiState(
    val brandId: Int = 0,
    val brandName: String = "",
    val motors: List<DataMotor> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val showDeleteDialog: Boolean = false,
    val motorToDelete: DataMotor? = null,
    val showStokDialog: Boolean = false,
    val motorForStok: DataMotor? = null,
    val isAddStok: Boolean = true
)