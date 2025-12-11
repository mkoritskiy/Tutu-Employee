package ru.tutu.tutuemployee.presentation.webview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.cValue
import platform.CoreGraphics.CGRectZero
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.WebKit.WKWebView
import platform.WebKit.WKWebViewConfiguration

/**
 * iOS реализация WebView компонента
 */
@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun WebViewComponent(
    url: String,
    modifier: Modifier
) {
    UIKitView(
        modifier = modifier,
        factory = {
            val config = WKWebViewConfiguration()
            val webView =
                WKWebView(frame = cValue { CGRectZero }, configuration = config)

            // Загружаем URL
            NSURL.URLWithString(url)?.let { nsUrl ->
                val request = NSURLRequest.requestWithURL(nsUrl)
                webView.loadRequest(request)
            }

            webView
        },
        update = { webView ->
            // Обновляем URL если он изменился
            NSURL.URLWithString(url)?.let { nsUrl ->
                val request = NSURLRequest.requestWithURL(nsUrl)
                webView.loadRequest(request)
            }
        }
    )
}
