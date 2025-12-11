package ru.tutu.tutuemployee.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import ru.tutu.tutuemployee.presentation.auth.AuthScreen
import ru.tutu.tutuemployee.presentation.favorites.FavoritesScreen
import ru.tutu.tutuemployee.presentation.home.HomeScreen
import ru.tutu.tutuemployee.presentation.merch.MerchScreen
import ru.tutu.tutuemployee.presentation.office.OfficeScreen
import ru.tutu.tutuemployee.presentation.profile.ProfileScreen
import ru.tutu.tutuemployee.presentation.webview.WebViewScreen

@Composable
fun NavigationHost(
    navController: NavHostController = rememberNavController(),
    startDestination: Screen = Screen.Auth
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Screen.Auth> {
            AuthScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home) {
                        popUpTo<Screen.Auth> { inclusive = true }
                    }
                }
            )
        }

        composable<Screen.Home> {
            HomeScreen(
                navController = navController,
                onNavigateToWebView = { url, title ->
                    navController.navigate(Screen.WebView(url, title))
                }
            )
        }

        composable<Screen.Profile> {
            ProfileScreen(
                navController = navController
            )
        }

        composable<Screen.Office> {
            OfficeScreen(
                navController = navController,
                onNavigateToWebView = { url, title ->
                    navController.navigate(Screen.WebView(url, title))
                }
            )
        }

        composable<Screen.Merch> {
            MerchScreen(
                navController = navController
            )
        }

        composable<Screen.Favorites> {
            FavoritesScreen(
                navController = navController,
                onNavigateToWebView = { url, title ->
                    navController.navigate(Screen.WebView(url, title))
                }
            )
        }

        composable<Screen.WebView> { backStackEntry ->
            val webViewScreen: Screen.WebView = backStackEntry.toRoute()
            WebViewScreen(
                url = webViewScreen.url,
                title = webViewScreen.title,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
