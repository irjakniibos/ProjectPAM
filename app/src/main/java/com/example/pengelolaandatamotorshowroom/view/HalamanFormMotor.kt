package com.example.pengelolaandatamotorshowroom.view

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.pengelolaandatamotorshowroom.R
import com.example.pengelolaandatamotorshowroom.ui.theme.*
import com.example.pengelolaandatamotorshowroom.viewmodel.FormMotorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanFormMotor(
    brandId: Int,
    brandName: String,
    motorId: Int?,
    onBack: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: FormMotorViewModel,
    modifier: Modifier = Modifier
) {
    val uiState = viewModel.uiState
    val scrollState = rememberScrollState()
    var tipeExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val tipeOptions = listOf(
        stringResource(R.string.type_matic),
        stringResource(R.string.type_sport),
        stringResource(R.string.type_bebek),
        stringResource(R.string.type_trail)
    )

    // Tampilkan toast ketika ada successMessage
    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.clearSuccessMessage()
        }
    }

    // Load data berdasarkan mode
    LaunchedEffect(motorId) {
        if (motorId != null && motorId > 0) {
            // Mode EDIT: Load data motor dari API
            viewModel.loadMotorForEdit(motorId, brandId, brandName)
        } else {
            // Mode TAMBAH: Set brand info saja
            viewModel.setFormData(brandId, brandName)
        }
    }

    Scaffold(
        topBar = {
            ShowroomTopAppBar(
                title = stringResource(
                    if (uiState.isEditMode) R.string.form_motor_edit_title else R.string.form_motor_add_title
                ),
                canNavigateBack = true,
                onNavigateBack = onBack
            )
        },
        containerColor = Background
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(dimensionResource(R.dimen.padding_medium))
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
        ) {
            // Show loading when fetching motor data for edit
            if (uiState.isLoading && uiState.motorId == null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(R.dimen.form_loading_height)),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    CircularProgressIndicator(color = Primary)
                }
            } else {
                // Brand (Read Only)
                OutlinedTextField(
                    value = uiState.brandName,
                    onValueChange = {},
                    label = { Text(stringResource(R.string.motor_brand)) },
                    enabled = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledBorderColor = OnBackground,
                        disabledTextColor = OnBackground,
                        disabledLabelColor = OnBackground
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Nama Motor
                OutlinedTextField(
                    value = uiState.namaMotor,
                    onValueChange = { viewModel.updateNamaMotor(it) },
                    label = { Text(stringResource(R.string.motor_name)) },
                    placeholder = { Text(stringResource(R.string.motor_name_hint)) },
                    isError = uiState.namaMotorError != null,
                    supportingText = {
                        if (uiState.namaMotorError != null) {
                            Text(uiState.namaMotorError, color = Error)
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = OnBackground,
                        focusedTextColor = OnBackground,
                        unfocusedTextColor = OnBackground,
                        cursorColor = Primary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Tipe (Dropdown)
                ExposedDropdownMenuBox(
                    expanded = tipeExpanded,
                    onExpandedChange = { tipeExpanded = !tipeExpanded }
                ) {
                    OutlinedTextField(
                        value = uiState.tipe,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.motor_type)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = tipeExpanded) },
                        isError = uiState.tipeError != null,
                        supportingText = {
                            if (uiState.tipeError != null) {
                                Text(uiState.tipeError, color = Error)
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Primary,
                            unfocusedBorderColor = OnBackground,
                            focusedTextColor = OnBackground,
                            unfocusedTextColor = OnBackground
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = tipeExpanded,
                        onDismissRequest = { tipeExpanded = false }
                    ) {
                        tipeOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    viewModel.updateTipe(option)
                                    tipeExpanded = false
                                }
                            )
                        }
                    }
                }

                // Tahun
                OutlinedTextField(
                    value = uiState.tahun,
                    onValueChange = { viewModel.updateTahun(it) },
                    label = { Text(stringResource(R.string.motor_year)) },
                    placeholder = { Text(stringResource(R.string.motor_year_hint)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = uiState.tahunError != null,
                    supportingText = {
                        if (uiState.tahunError != null) {
                            Text(uiState.tahunError, color = Error)
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = OnBackground,
                        focusedTextColor = OnBackground,
                        unfocusedTextColor = OnBackground,
                        cursorColor = Primary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Harga
                OutlinedTextField(
                    value = uiState.harga,
                    onValueChange = { viewModel.updateHarga(it) },
                    label = { Text(stringResource(R.string.motor_price)) },
                    placeholder = { Text(stringResource(R.string.motor_price_hint)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = uiState.hargaError != null,
                    supportingText = {
                        if (uiState.hargaError != null) {
                            Text(uiState.hargaError, color = Error)
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = OnBackground,
                        focusedTextColor = OnBackground,
                        unfocusedTextColor = OnBackground,
                        cursorColor = Primary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Warna
                OutlinedTextField(
                    value = uiState.warna,
                    onValueChange = { viewModel.updateWarna(it) },
                    label = { Text(stringResource(R.string.motor_color)) },
                    placeholder = { Text(stringResource(R.string.motor_color_hint)) },
                    isError = uiState.warnaError != null,
                    supportingText = {
                        if (uiState.warnaError != null) {
                            Text(uiState.warnaError, color = Error)
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = OnBackground,
                        focusedTextColor = OnBackground,
                        unfocusedTextColor = OnBackground,
                        cursorColor = Primary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Stok Awal (only for add mode)
                if (!uiState.isEditMode) {
                    OutlinedTextField(
                        value = uiState.stokAwal,
                        onValueChange = { viewModel.updateStokAwal(it) },
                        label = { Text(stringResource(R.string.stok_awal)) },
                        placeholder = { Text(stringResource(R.string.motor_stock_hint)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = uiState.stokAwalError != null,
                        supportingText = {
                            if (uiState.stokAwalError != null) {
                                Text(uiState.stokAwalError, color = Error)
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Primary,
                            unfocusedBorderColor = OnBackground,
                            focusedTextColor = OnBackground,
                            unfocusedTextColor = OnBackground,
                            cursorColor = Primary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Error Message
                if (uiState.errorMessage != null) {
                    Text(
                        text = uiState.errorMessage,
                        color = Error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
                ) {
                    Button(
                        onClick = onBack,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Disabled,
                            contentColor = OnDisabled
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.btn_cancel))
                    }

                    Button(
                        onClick = { viewModel.saveMotor(onSuccess) },
                        enabled = !uiState.isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Primary,
                            contentColor = OnPrimary
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                color = OnPrimary,
                                modifier = Modifier.size(dimensionResource(R.dimen.icon_size_action))
                            )
                        } else {
                            Text(stringResource(R.string.btn_save))
                        }
                    }
                }
            }
        }
    }
}