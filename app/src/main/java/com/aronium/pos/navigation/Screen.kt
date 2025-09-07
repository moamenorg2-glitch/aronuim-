package com.aronium.pos.navigation

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Orders : Screen("orders")
    object Products : Screen("products")
    object Recipes : Screen("recipes")
    object Customers : Screen("customers")
    object Reports : Screen("reports")
    object Settings : Screen("settings")
}