package ru.tutu.tutuemployee

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import ru.tutu.tutuemployee.navigation.NavigationHost
import ru.tutu.tutuemployee.navigation.Screen

@Composable
fun App() {
    MaterialTheme {
        NavigationHost(
            startDestination = Screen.Auth
        )
    }
}
