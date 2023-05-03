package com.example.musicx.presentation.util

sealed class Screen(val route: String) {
    object Home : Screen(route = "home")
    object Player : Screen(route = "player")
    object Favourites : Screen(route = "favourites")
}