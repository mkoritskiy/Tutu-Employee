package ru.tutu.tutuemployee.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.tutu.tutuemployee.navigation.Screen
import kotlin.reflect.KClass

sealed class BottomNavItem(
    val screen: Screen,
    val icon: ImageVector,
    val label: String
) {
    data object Home : BottomNavItem(Screen.Home, Icons.Default.Home, "Главная")
    data object Profile : BottomNavItem(Screen.Profile, Icons.Default.Person, "Профиль")
    data object Office : BottomNavItem(Screen.Office, Icons.Default.Business, "Офис")
    data object Merch : BottomNavItem(Screen.Merch, Icons.Default.ShoppingCart, "Шоп")
    data object Favorites : BottomNavItem(Screen.Favorites, Icons.Default.Star, "Избранное")
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
                icon = { Icon(item.icon, contentDescription = item.label) },
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
