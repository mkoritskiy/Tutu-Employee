package ru.tutu.tutuemployee.presentation.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import ru.tutu.tutuemployee.MainActivity

/**
 * Android реализация обработки OAuth callback через Deep Links
 */
@Composable
actual fun SetupOAuthCallbackHandler(onCallback: (String) -> Unit) {
    val context = LocalContext.current

    DisposableEffect(Unit) {
        val activity = context as? MainActivity
        activity?.setOAuthCallback(onCallback)

        onDispose {
            activity?.setOAuthCallback(null)
        }
    }
}
