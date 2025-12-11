package ru.tutu.tutuemployee.presentation.webview

import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebChromeClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

/**
 * Android реализация WebView компонента
 */
@Composable
actual fun WebViewComponent(
    url: String,
    modifier: Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                // Настройки WebView
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    setSupportZoom(true)
                    builtInZoomControls = true
                    displayZoomControls = false
                    useWideViewPort = true
                    loadWithOverviewMode = true
                }

                // WebViewClient для обработки навигации
                webViewClient = WebViewClient()

                // WebChromeClient для обработки прогресса загрузки
                webChromeClient = WebChromeClient()

                // Загружаем URL
                loadUrl(url)
            }
        },
        update = { webView ->
            // Обновляем URL если он изменился
            if (webView.url != url) {
                webView.loadUrl(url)
            }
        }
    )
}
