package com.yeasinx.oshudhkhan.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.yeasinx.oshudhkhan.ui.medicine.MedicineViewModel
import com.yeasinx.oshudhkhan.ui.screens.AddMedicineScreen
import com.yeasinx.oshudhkhan.ui.screens.MedicineListScreen

sealed class Screen(val route: String) {
    object MedicineList : Screen("medicine_list")
    object AddMedicine : Screen("add_medicine")
}

@Composable
fun MedicineNavGraph(
    navController: NavHostController,
    viewModel: MedicineViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.MedicineList.route
    ) {
        composable(Screen.MedicineList.route) {
            MedicineListScreen(
                viewModel = viewModel,
                onAddMedicine = { navController.navigate(Screen.AddMedicine.route) }
            )
        }
        composable(Screen.AddMedicine.route) {
            AddMedicineScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
                )
        }
    }
}