package com.example.pengelolaandatamotorshowroom.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.pengelolaandatamotorshowroom.ui.theme.OnPrimary
import com.example.pengelolaandatamotorshowroom.ui.theme.Primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowroomTopAppBar(
    title: String,
    canNavigateBack: Boolean = false,
    onNavigateBack: () -> Unit = {},
    showLogout: Boolean = false,
    onLogout: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                color = OnPrimary,
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Kembali",
                        tint = OnPrimary
                    )
                }
            }
        },
        actions = {
            if (showLogout) {
                IconButton(onClick = onLogout) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Logout",
                        tint = OnPrimary
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Primary,
            titleContentColor = OnPrimary
        )
    )
}