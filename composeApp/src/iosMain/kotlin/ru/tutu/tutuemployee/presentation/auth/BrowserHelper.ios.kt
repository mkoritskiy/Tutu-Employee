package ru.tutu.tutuemployee.presentation.auth

import androidx.compose.runtime.Composable
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

/**
 * iOS реализация открытия URL в браузере
 * Использует UIApplication для открытия Safari
 */
actual fun openUrlInBrowser(url: String) {
    val nsUrl = NSURL.URLWithString(url)
    if (nsUrl != null) {
        UIApplication.sharedApplication.openURL(nsUrl)
    }
}

/**
 * iOS поддерживает Deep Links через URL Schemes и Universal Links
 */
actual fun canHandleDeepLinks(): Boolean = true

/**
 * iOS реализация обработки OAuth callback
 * Требуется дополнительная настройка в iOS проекте для обработки URL Schemes
 * Callback должен быть зарегистрирован в AppDelegate
 */
@Composable
actual fun SetupOAuthCallbackHandler(onCallback: (String) -> Unit) {
    // TODO: Реализовать обработку через NotificationCenter или через AppDelegate
    // В iOS это делается через application(_:open:options:) в AppDelegate
}
