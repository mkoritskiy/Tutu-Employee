package ru.tutu.tutuemployee.presentation.webview

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Платформо-специфичный WebView компонент
 */
@Composable
expect fun WebViewComponent(
    url: String,
    modifier: Modifier = Modifier
)
