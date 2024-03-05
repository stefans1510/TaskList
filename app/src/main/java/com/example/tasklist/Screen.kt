package com.example.tasklist

sealed class Screen(val route: String) {
    object HomeScreen: Screen("home_screen")
    object CreateScreen: Screen("create_screen")
}
