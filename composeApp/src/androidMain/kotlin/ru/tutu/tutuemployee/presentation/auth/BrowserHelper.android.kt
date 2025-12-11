package ru.tutu.tutuemployee.presentation.auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent

/**
 * Глобальный контекст для Android
 * Устанавливается в Application классе
 */
object AndroidContextProvider {
    lateinit var applicationContext: Context
}

/**
 * Android реализация открытия URL в браузере
 * Использует Chrome Custom Tabs для лучшего UX
 */
actual fun openUrlInBrowser(url: String) {
    val context = AndroidContextProvider.applicationContext

    try {
        // Пытаемся использовать Chrome Custom Tabs для лучшего UX
        val customTabsIntent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .build()

        customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        customTabsIntent.launchUrl(context, Uri.parse(url))
    } catch (e: Exception) {
        // Fallback на обычный браузер
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

/**
 * Android поддерживает Deep Links через Intent Filters
 */
actual fun canHandleDeepLinks(): Boolean = true
