package com.example.attendmestudents.navigation

sealed class Screens(val route: String) {
    object LoginScreen : Screens("login_screen")
    object RegisterScreen : Screens("register_screen")
    object HomeScreen : Screens("home_screen")
}