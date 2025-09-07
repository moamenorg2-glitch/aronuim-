package com.aronium.pos.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.aronium.pos.ui.screens.dashboard.DashboardScreen
import com.aronium.pos.ui.screens.orders.OrdersScreen
import com.aronium.pos.ui.screens.products.ProductsScreen
import com.aronium.pos.ui.screens.recipes.RecipesScreen
import com.aronium.pos.ui.screens.customers.CustomersScreen
import com.aronium.pos.ui.screens.reports.ReportsScreen
import com.aronium.pos.ui.screens.settings.SettingsScreen

@Composable
fun AroniumNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route,
        modifier = modifier
    ) {
        composable(Screen.Dashboard.route) {
            DashboardScreen()
        }
        composable(Screen.Orders.route) {
            OrdersScreen()
        }
        composable(Screen.Products.route) {
            ProductsScreen()
        }
        composable(Screen.Recipes.route) {
            RecipesScreen()
        }
        composable(Screen.Customers.route) {
            CustomersScreen()
        }
        composable(Screen.Reports.route) {
            ReportsScreen()
        }
        composable(Screen.Settings.route) {
            SettingsScreen()
        }
    }
}