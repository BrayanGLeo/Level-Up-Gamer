package com.example.levelupgamer.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object ProductList : Screen("product_list")
    object Cart : Screen("cart")

    object Home : Screen("home")
    object Profile : Screen("profile")
    object Orders : Screen("orders")
}