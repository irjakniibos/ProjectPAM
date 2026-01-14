package com.example.pengelolaandatamotorshowroom.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pengelolaandatamotorshowroom.modeldata.DataMotor
import com.example.pengelolaandatamotorshowroom.repositori.RepositoryMotor
import kotlinx.coroutines.launch

class FormMotorViewModel(private val repositoryMotor: RepositoryMotor) : ViewModel() {

    var uiState by mutableStateOf(FormMotorUiState())
        private set

    // Untuk mode TAMBAH - set brand info saja
    fun setFormData(brandId: Int, brandName: String) {
        uiState = uiState.copy(
            isEditMode = false,
            brandId = brandId,
            brandName = brandName,
            motorId = null,
            namaMotor = "",
            tipe = "",
            tahun = "",
            harga = "",
            warna = "",
            stokAwal = "0"
        )
    }

    // Untuk mode EDIT - load data motor dari API
    fun loadMotorForEdit(motorId: Int, brandId: Int, brandName: String) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            try {
                val motor = repositoryMotor.getMotorDetail(motorId)
                uiState = uiState.copy(
                    isLoading = false,
                    isEditMode = true,
                    motorId = motor.id,
                    brandId = brandId,
                    brandName = brandName,
                    namaMotor = motor.nama_motor,
                    tipe = motor.tipe,
                    tahun = motor.tahun.toString(),
                    harga = motor.harga.toInt().toString(), // Convert to Int string tanpa desimal
                    warna = motor.warna
                )
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "Gagal memuat data motor: ${e.message}"
                )
            }
        }
    }

    fun updateNamaMotor(value: String) {
        uiState = uiState.copy(namaMotor = value, namaMotorError = null)
    }

    fun updateTipe(value: String) {
        uiState = uiState.copy(tipe = value, tipeError = null)
    }

    fun updateTahun(value: String) {
        uiState = uiState.copy(tahun = value, tahunError = null)
    }

    fun updateHarga(value: String) {
        // Allow only digits
        if (value.isEmpty() || value.all { it.isDigit() }) {
            uiState = uiState.copy(harga = value, hargaError = null)
        }
    }

    fun updateWarna(value: String) {
        uiState = uiState.copy(warna = value, warnaError = null)
    }

    fun updateStokAwal(value: String) {
        // Allow only digits
        if (value.isEmpty() || value.all { it.isDigit() }) {
            uiState = uiState.copy(stokAwal = value, stokAwalError = null)
        }
    }

    fun saveMotor(onSuccess: () -> Unit) {
        // Validasi
        var hasError = false

        if (uiState.namaMotor.isBlank()) {
            uiState = uiState.copy(namaMotorError = "Nama motor tidak boleh kosong")
            hasError = true
        }
        if (uiState.tipe.isBlank()) {
            uiState = uiState.copy(tipeError = "Tipe tidak boleh kosong")
            hasError = true
        }
        if (uiState.tahun.isBlank()) {
            uiState = uiState.copy(tahunError = "Tahun tidak boleh kosong")
            hasError = true
        } else {
            val tahunInt = uiState.tahun.toIntOrNull()
            if (tahunInt == null || tahunInt < 2000) {
                uiState = uiState.copy(tahunError = "Tahun minimal 2000")
                hasError = true
            }
        }
        if (uiState.harga.isBlank()) {
            uiState = uiState.copy(hargaError = "Harga tidak boleh kosong")
            hasError = true
        } else {
            val hargaDouble = uiState.harga.toDoubleOrNull()
            if (hargaDouble == null || hargaDouble <= 0) {
                uiState = uiState.copy(hargaError = "Harga harus lebih dari 0")
                hasError = true
            }
        }
        if (uiState.warna.isBlank()) {
            uiState = uiState.copy(warnaError = "Warna tidak boleh kosong")
            hasError = true
        }
        if (!uiState.isEditMode && uiState.stokAwal.isBlank()) {
            uiState = uiState.copy(stokAwalError = "Stok awal tidak boleh kosong")
            hasError = true
        } else if (!uiState.isEditMode) {
            val stokInt = uiState.stokAwal.toIntOrNull()
            if (stokInt == null || stokInt < 0) {
                uiState = uiState.copy(stokAwalError = "Stok awal tidak boleh negatif")
                hasError = true
            }
        }

        if (hasError) return

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            try {
                if (uiState.isEditMode) {
                    repositoryMotor.updateMotor(
                        id = uiState.motorId!!,
                        namaMotor = uiState.namaMotor,
                        brandId = uiState.brandId,
                        tipe = uiState.tipe,
                        tahun = uiState.tahun.toInt(),
                        harga = uiState.harga.toDouble(),
                        warna = uiState.warna
                    )
                } else {
                    repositoryMotor.createMotor(
                        namaMotor = uiState.namaMotor,
                        brandId = uiState.brandId,
                        tipe = uiState.tipe,
                        tahun = uiState.tahun.toInt(),
                        harga = uiState.harga.toDouble(),
                        warna = uiState.warna,
                        stokAwal = uiState.stokAwal.toInt()
                    )
                }
                uiState = uiState.copy(isLoading = false)
                onSuccess()
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }
}

data class FormMotorUiState(
    val isEditMode: Boolean = false,
    val motorId: Int? = null,
    val brandId: Int = 0,
    val brandName: String = "",
    val namaMotor: String = "",
    val tipe: String = "",
    val tahun: String = "",
    val harga: String = "",
    val warna: String = "",
    val stokAwal: String = "0",
    val namaMotorError: String? = null,
    val tipeError: String? = null,
    val tahunError: String? = null,
    val hargaError: String? = null,
    val warnaError: String? = null,
    val stokAwalError: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)