package com.example.pengelolaandatamotorshowroom.uicontroller.route

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pengelolaandatamotorshowroom.view.*
import com.example.pengelolaandatamotorshowroom.viewmodel.provider.PenyediaViewModel

@Composable
fun PetaNavigasi(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = DestinasiLogin.route
    ) {
        // Login
        composable(DestinasiLogin.route) {
            HalamanLogin(
                onLoginSuccess = {
                    navController.navigate(DestinasiDashboard.route) {
                        popUpTo(DestinasiLogin.route) { inclusive = true }
                    }
                },
                viewModel = viewModel(factory = PenyediaViewModel.Factory)
            )
        }

        // Dashboard
        composable(DestinasiDashboard.route) {
            HalamanDashboard(
                onBrandClick = { brandId, brandName ->
                    navController.navigate(DestinasiListMotor.createRoute(brandId, brandName))
                },
                onLogout = {
                    navController.navigate(DestinasiLogin.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                viewModel = viewModel(factory = PenyediaViewModel.Factory)
            )
        }

        // List Motor
        composable(
            route = DestinasiListMotor.routeWithArgs,
            arguments = listOf(
                navArgument(DestinasiListMotor.BRAND_ID) { type = NavType.IntType },
                navArgument(DestinasiListMotor.BRAND_NAME) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val brandId = backStackEntry.arguments?.getInt(DestinasiListMotor.BRAND_ID) ?: 0
            val brandName = backStackEntry.arguments?.getString(DestinasiListMotor.BRAND_NAME) ?: ""

            HalamanListMotor(
                brandId = brandId,
                brandName = brandName,
                onBack = { navController.popBackStack() },
                onAddMotor = {
                    navController.navigate(DestinasiFormMotor.createRouteAdd(brandId, brandName))
                },
                onDetailClick = { motorId ->
                    navController.navigate(DestinasiDetailMotor.createRoute(motorId))
                },
                viewModel = viewModel(factory = PenyediaViewModel.Factory)
            )
        }

        // Detail Motor
        composable(
            route = DestinasiDetailMotor.routeWithArgs,
            arguments = listOf(
                navArgument(DestinasiDetailMotor.MOTOR_ID) { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val motorId = backStackEntry.arguments?.getInt(DestinasiDetailMotor.MOTOR_ID) ?: 0

            HalamanDetailMotor(
                motorId = motorId,
                onBack = { navController.popBackStack() },
                onEdit = { motor ->
                    navController.navigate(
                        DestinasiFormMotor.createRouteEdit(motor.brand_id, motor.nama_brand, motor.id)
                    )
                },
                viewModel = viewModel(factory = PenyediaViewModel.Factory)
            )
        }

        // Form Motor (Add/Edit)
        composable(
            route = DestinasiFormMotor.routeWithArgs,
            arguments = listOf(
                navArgument(DestinasiFormMotor.BRAND_ID) { type = NavType.IntType },
                navArgument(DestinasiFormMotor.BRAND_NAME) { type = NavType.StringType },
                navArgument(DestinasiFormMotor.MOTOR_ID) {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) { backStackEntry ->
            val brandId = backStackEntry.arguments?.getInt(DestinasiFormMotor.BRAND_ID) ?: 0
            val brandName = backStackEntry.arguments?.getString(DestinasiFormMotor.BRAND_NAME) ?: ""
            val motorId = backStackEntry.arguments?.getInt(DestinasiFormMotor.MOTOR_ID) ?: 0

            HalamanFormMotor(
                brandId = brandId,
                brandName = brandName,
                motorId = if (motorId == 0) null else motorId,
                onBack = { navController.popBackStack() },
                onSuccess = { navController.popBackStack() },
                viewModel = viewModel(factory = PenyediaViewModel.Factory)
            )
        }
    }
}