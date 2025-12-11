package ru.tutu.tutuemployee.presentation.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.tutu.tutuemployee.navigation.Screen
import kotlin.reflect.KClass

sealed class BottomNavItem(
    val screen: Screen,
    val icon: String,
    val label: String
) {
    data object Home : BottomNavItem(Screen.Home, "🏠", "Главная")
    data object Profile : BottomNavItem(Screen.Profile, "👤", "Профиль")
    data object Office : BottomNavItem(Screen.Office, "🏢", "Офис")
    data object Merch : BottomNavItem(Screen.Merch, "🛒", "Мерч")
    data object Favorites : BottomNavItem(Screen.Favorites, "⭐", "Избранное")
}

@Composable
fun BottomNavigationBar(
    navController: NavHostController
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Profile,
        BottomNavItem.Office,
        BottomNavItem.Merch,
        BottomNavItem.Favorites
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        items.forEach { item ->
            val isSelected = currentDestination?.hasRoute(item.screen::class) ?: false

            NavigationBarItem(
                icon = { Text(item.icon, style = MaterialTheme.typography.titleLarge) },
                label = { Text(item.label) },
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        navController.navigate(item.screen) {
                            // Избегаем создания множественных копий одного экрана
                            popUpTo(Screen.Home) {
                                saveState = true
                            }
                            // Избегаем множественных копий одного и того же экрана
                            launchSingleTop = true
                            // Восстанавливаем состояние при возврате к ранее выбранному элементу
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}
