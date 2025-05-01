package kz.qamshy.app.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import kz.qamshy.app.ui.QamshyApp


@Composable
fun WebViewWithHeaders(
    url: String,
    extraHeaders: Map<String, String>,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
            }
        },
        update = { webView ->
            webView.loadUrl(url, extraHeaders)
        }
    )
}