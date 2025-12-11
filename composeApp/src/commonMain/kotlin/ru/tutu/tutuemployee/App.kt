package ru.tutu.tutuemployee

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import org.koin.compose.KoinContext
import ru.tutu.tutuemployee.navigation.NavigationHost
import ru.tutu.tutuemployee.navigation.Screen

@Composable
fun App() {
    KoinContext {
        MaterialTheme {
            NavigationHost(
                startDestination = Screen.Home
            )
        }
    }
}
