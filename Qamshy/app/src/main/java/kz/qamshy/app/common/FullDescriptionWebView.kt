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
fun SafeFullDescriptionWebView(
    html: String,
    modifier: Modifier = Modifier,
    context:Context
) {

    AndroidView(
        factory = { ctx ->
            WebView(ctx).apply {
                settings.apply {
                    // 必要的设置
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    useWideViewPort = true
                    loadWithOverviewMode = true

                    // 增强安全性
                    allowFileAccess = false
                    allowContentAccess = false
                    allowFileAccessFromFileURLs = false
                    allowUniversalAccessFromFileURLs = false

                    // 混合内容处理
                    mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE

                    // 适配视图
                    builtInZoomControls = true
                    displayZoomControls = false
                }

                // 安全的WebViewClient
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                        val url = request.url.toString()
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                        return true
                    }

                    override fun onPageFinished(view: WebView, url: String) {
                        super.onPageFinished(view, url)

                        // 注入CSS以改善阅读体验
                        val css = """
                            body {
                                font-family: sans-serif;
                                line-height: 1.5;
                                font-size: 16px;
                                padding: 16px;
                                color: #222;
                                background-color: transparent;
                            }
                            img, video, iframe {
                                max-width: 100%;
                                height: auto;
                            }
                        """.trimIndent()

                        view.evaluateJavascript(
                            "(function() {" +
                                    "   var style = document.createElement('style');" +
                                    "   style.type = 'text/css';" +
                                    "   style.innerHTML = '$css';" +
                                    "   document.head.appendChild(style);" +
                                    "})();",
                            null
                        )
                    }
                }
            }
        },
        update = { webView ->
            // 添加响应式视口元标记
            val htmlWithViewport = if (!html.contains("<meta name=\"viewport\"")) {
                "<html><head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"></head><body>$html</body></html>"
            } else {
                html
            }

            // 使用应用域名作为baseUrl
            webView.loadDataWithBaseURL(
                QamshyApp.siteUrl,
                htmlWithViewport,
                "text/html",
                "utf-8",
                null
            )
        },
        modifier = modifier
    )
}


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