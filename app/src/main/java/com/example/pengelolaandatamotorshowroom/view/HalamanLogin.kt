package com.example.pengelolaandatamotorshowroom.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pengelolaandatamotorshowroom.R
import com.example.pengelolaandatamotorshowroom.ui.theme.*
import com.example.pengelolaandatamotorshowroom.viewmodel.LoginViewModel

@Composable
fun HalamanLogin(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel,
    modifier: Modifier = Modifier
) {
    val uiState = viewModel.uiState
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val loginSuccessMessage = stringResource(R.string.login_success)

    // Tampilkan toast ketika login berhasil
    LaunchedEffect(uiState.showSuccessToast) {
        if (uiState.showSuccessToast) {
            Toast.makeText(context, loginSuccessMessage, Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.backgroundone),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Content dengan semi-transparent overlay (opsional)
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Background.copy(alpha = 0.7f) // Sesuaikan alpha untuk transparansi
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(R.dimen.padding_large)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Logo Showroom Motor
                Image(
                    painter = painterResource(id = R.drawable.logoshowroommotor),
                    contentDescription = "Logo Showroom Motor",
                    modifier = Modifier.size(dimensionResource(R.dimen.login_icon_box_size)),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_large)))

                // Title - 2 kata
                Text(
                    text = stringResource(R.string.login_showroom),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = dimensionResource(R.dimen.text_size_display).value.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = OnBackground,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(R.string.login_motor),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = dimensionResource(R.dimen.text_size_display).value.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = OnBackground,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))

                // Teks Deskripsi
                Text(
                    text = stringResource(R.string.login_description),
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnBackground.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium))
                )

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_extra_large)))

                // Card untuk form login
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = CardBackground
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.card_elevation_high)),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.dialog_corner_radius))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(dimensionResource(R.dimen.padding_large)),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Email TextField
                        OutlinedTextField(
                            value = uiState.email,
                            onValueChange = { viewModel.updateEmail(it) },
                            label = { Text(stringResource(R.string.email)) },
                            placeholder = { Text(stringResource(R.string.email_placeholder)) },
                            isError = uiState.emailError != null,
                            supportingText = {
                                if (uiState.emailError != null) {
                                    Text(uiState.emailError, color = Error)
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Primary,
                                unfocusedBorderColor = CardOnBackground.copy(alpha = 0.5f),
                                focusedTextColor = CardOnBackground,
                                unfocusedTextColor = CardOnBackground,
                                focusedLabelColor = Primary,
                                unfocusedLabelColor = CardOnBackground.copy(alpha = 0.7f),
                                cursorColor = Primary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))

                        // Password TextField
                        OutlinedTextField(
                            value = uiState.password,
                            onValueChange = { viewModel.updatePassword(it) },
                            label = { Text(stringResource(R.string.password)) },
                            placeholder = { Text(stringResource(R.string.password_placeholder)) },
                            isError = uiState.passwordError != null,
                            supportingText = {
                                if (uiState.passwordError != null) {
                                    Text(uiState.passwordError, color = Error)
                                }
                            },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                        tint = CardOnBackground
                                    )
                                }
                            },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Primary,
                                unfocusedBorderColor = CardOnBackground.copy(alpha = 0.5f),
                                focusedTextColor = CardOnBackground,
                                unfocusedTextColor = CardOnBackground,
                                focusedLabelColor = Primary,
                                unfocusedLabelColor = CardOnBackground.copy(alpha = 0.7f),
                                cursorColor = Primary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))

                        // Error Message
                        if (uiState.errorMessage != null) {
                            Text(
                                text = uiState.errorMessage,
                                color = Error,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))
                        }

                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_large)))

                        // Login Button
                        Button(
                            onClick = { viewModel.login(onLoginSuccess) },
                            enabled = !uiState.isLoading && uiState.email.isNotBlank() && uiState.password.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Primary,
                                contentColor = OnPrimary
                            ),
                            shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(dimensionResource(R.dimen.textfield_height))
                        ) {
                            if (uiState.isLoading) {
                                CircularProgressIndicator(
                                    color = OnPrimary,
                                    modifier = Modifier.size(dimensionResource(R.dimen.icon_size_medium))
                                )
                            } else {
                                Text(
                                    stringResource(R.string.btn_login),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}