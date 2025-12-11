package ru.tutu.tutuemployee.presentation.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.browser.window

/**
 * WASM реализация открытия URL
 * Использует window.open для открытия в новом окне/вкладке
 */
actual fun openUrlInBrowser(url: String) {
    window.open(url, "_blank")
}

/**
 * Web браузеры поддерживают редиректы через callback URL
 */
actual fun canHandleDeepLinks(): Boolean = true

/**
 * WASM реализация обработки OAuth callback
 * В web приложении обычно используется redirect back или postMessage
 */
@Composable
actual fun SetupOAuthCallbackHandler(onCallback: (String) -> Unit) {
    // В web приложении OAuth callback обрабатывается через window.location
    // или через postMessage от popup окна
    LaunchedEffect(Unit) {
        // TODO: Реализовать обработку через window.addEventListener для postMessage
        // или проверку window.location при возврате
    }
}
