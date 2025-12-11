package ru.tutu.tutuemployee.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Auth : Screen

    @Serializable
    data object Home : Screen

    @Serializable
    data object Profile : Screen

    @Serializable
    data object Office : Screen

    @Serializable
    data object Merch : Screen

    @Serializable
    data object Favorites : Screen

    @Serializable
    data class WebView(val url: String, val title: String) : Screen
}
