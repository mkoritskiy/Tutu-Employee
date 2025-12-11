package ru.tutu.tutuemployee

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat

class MainActivity : ComponentActivity() {

    // Callback для обработки Deep Links из OAuth
    private var oauthCallback: ((String) -> Unit)? = null

    fun setOAuthCallback(callback: ((String) -> Unit)?) {
        oauthCallback = callback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Настройка светлых системных значков для темного тулбара
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = false // false = светлые иконки (для темного фона)
            isAppearanceLightNavigationBars = false // false = светлые иконки в навигационной панели
        }

        // Обработка Deep Link при создании Activity
        handleIntent(intent)

        setContent {
            App()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        // Обработка Deep Link при получении нового Intent
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        val data: Uri? = intent?.data
        if (data != null) {
            val url = data.toString()
            // Проверяем, что это callback от Keycloak
            if (url.startsWith("tutuemployee://oauth/callback")) {
                oauthCallback?.invoke(url)
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}