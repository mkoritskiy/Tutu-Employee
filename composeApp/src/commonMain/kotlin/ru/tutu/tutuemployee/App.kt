package ru.tutu.tutuemployee

import androidx.compose.runtime.*
import org.koin.compose.KoinContext
import ru.tutu.tutuemployee.navigation.NavigationHost
import ru.tutu.tutuemployee.navigation.Screen
import ru.tutu.tutuemployee.ui.theme.TutuEmployeeTheme

@Composable
fun App() {
    KoinContext {
        TutuEmployeeTheme {
            NavigationHost(
                startDestination = Screen.Home
            )
        }
    }
}
