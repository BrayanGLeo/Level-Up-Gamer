package com.example.levelupgamer.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object ProductList : Screen("catalogo")
    object Blog : Screen("blog")
    object Cart : Screen("cart")
    object Login : Screen("login")
    object Register : Screen("register")

    object Profile : Screen("profile")
    object Orders : Screen("orders")
    object Addresses : Screen("addresses")
    object Settings : Screen("settings")
}