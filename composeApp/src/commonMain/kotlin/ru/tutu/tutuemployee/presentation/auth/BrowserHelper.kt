package ru.tutu.tutuemployee.presentation.auth

import androidx.compose.runtime.Composable

/**
 * Открыть URL в браузере
 * Platform-specific реализация
 */
expect fun openUrlInBrowser(url: String)

/**
 * Проверить, может ли платформа обрабатывать Deep Links / Custom URL Schemes
 */
expect fun canHandleDeepLinks(): Boolean

/**
 * Настроить обработку OAuth callback
 * Platform-specific реализация
 */
@Composable
expect fun SetupOAuthCallbackHandler(onCallback: (String) -> Unit)
