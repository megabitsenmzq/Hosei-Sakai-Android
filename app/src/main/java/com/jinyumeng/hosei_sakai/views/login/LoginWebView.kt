package com.jinyumeng.hosei_sakai.views.login

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.jinyumeng.hosei_sakai.hoppii.HoppiiURLs

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun LoginWebView(
    url: String,
    username: String?,
    password: String?,
    onPageStarted: ((String, String) -> Unit)? = null,
    onFinished: ((String) -> Unit)? = null,
    onError: ((String) -> Unit)? = null,
    onLoginError: (() -> Unit)? = null
){
    AndroidView(factory = { context ->
        WebView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            webViewClient = MyWebViewClient(
                onPageStarted = onPageStarted,
                onFinished = onFinished,
                onError = onError,
                onLoginError = onLoginError
            )
            settings.javaScriptEnabled = true
            loadUrl(url)
        }
    }, update = { webView ->
        if (url != webView.url) {
            webView.loadUrl(url)
        }
        if (username != null && password != null) {
            val inputStream = webView.context?.assets?.open("javascripts/AutoLogin.js")
            val autoLoginString = inputStream?.bufferedReader().use { it?.readText() }
            if (autoLoginString != null && username != "" && password != "") {
                Log.d("LoginWebView", "check saved: $username, $password")
                val scriptWithID = String.format(autoLoginString, username, password)
                webView.evaluateJavascript(scriptWithID, null)
            }
        } else {
            Log.d("LoginWebView", "no saved")
        }
    })
}

class MyWebViewClient(
    onPageStarted: ((String, String) -> Unit)?,
    onFinished: ((String) -> Unit)?,
    onError: ((String) -> Unit)?,
    onLoginError: (() -> Unit)?
) : WebViewClient() {

    private var onPageStarted: ((String, String) -> Unit)? = null
    private var onFinished: ((String) -> Unit)? = null
    private var onError: ((String) -> Unit)? = null
    private var onLoginError: (() -> Unit)? = null

    private val cookieManager = CookieManager.getInstance()

    init {
        this.onPageStarted = onPageStarted
        this.onFinished = onFinished
        this.onError = onError
        this.onLoginError = onLoginError

        cookieManager.setAcceptCookie(true)
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        val cookieString = cookieManager.getCookie(HoppiiURLs.Root.string) ?: ""
        onPageStarted?.let { it(url ?: "", cookieString) }
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)

        if ((url ?: "").startsWith(HoppiiURLs.HoseiSSO.string)) {
            val checkLoginState = "document.getElementsByClassName('form-error').length > 0;"
            view?.evaluateJavascript(checkLoginState) {
                if (it.toBoolean()) {
                    onLoginError?.let { it1 -> it1() }
                }
            }
        }

        onFinished?.let { it(url ?: "") }
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
        onError?.let { it(error?.description?.toString() ?: "") }
    }
}