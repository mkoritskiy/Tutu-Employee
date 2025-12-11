package ru.tutu.tutuemployee.presentation.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

sealed class BottomNavItem(
    val route: String,
    val icon: String,
    val label: String
) {
    data object Home : BottomNavItem("home", "🏠", "Главная")
    data object Profile : BottomNavItem("profile", "👤", "Профиль")
    data object Office : BottomNavItem("office", "🏢", "Офис")
    data object Merch : BottomNavItem("merch", "🛒", "Мерч")
    data object Favorites : BottomNavItem("favorites", "⭐", "Избранное")
}

@Composable
fun BottomNavigationBar(
    navController: Any, // Placeholder for now
    currentRoute: String
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Profile,
        BottomNavItem.Office,
        BottomNavItem.Merch,
        BottomNavItem.Favorites
    )

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Text(item.icon, style = MaterialTheme.typography.titleLarge) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    // Navigation will be implemented when navigation library is properly set up
                }
            )
        }
    }
}
