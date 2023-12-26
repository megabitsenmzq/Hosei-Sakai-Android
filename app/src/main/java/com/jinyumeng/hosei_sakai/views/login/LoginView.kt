package com.jinyumeng.hosei_sakai.views.login

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.jinyumeng.hosei_sakai.hoppii.HoppiiURLs
import com.jinyumeng.hosei_sakai.hoppii.LoginManager
import com.jinyumeng.hosei_sakai.views.common.LoadingAnimationView
import com.jinyumeng.hosei_sakai.views.common.NetworkErrorView
import java.util.Timer
import kotlin.concurrent.schedule

@Composable
fun LoginView() {
    var username by remember { mutableStateOf(LoginManager.username ?: "") }
    var password by remember { mutableStateOf(LoginManager.password ?: "") }

    var loadingFirstPage by remember { mutableStateOf(true) }
    var showLoading by remember { mutableStateOf(true) }
    var loginError by remember { mutableStateOf(false) }
    var loadError by remember { mutableStateOf(false) }

    var timeoutTimer: Timer? by remember { mutableStateOf(null) }

    // WebView
    var loginUrl: String by remember { mutableStateOf(HoppiiURLs.RequestLogin.string) }
    var webUsername: String? by remember { mutableStateOf("") }
    var webPassword: String? by remember { mutableStateOf("") }

    val handler = Handler(Looper.getMainLooper())

    fun setTimeoutTimer() {
        Log.d("LoginView", "Set timeout timer")
        if (timeoutTimer != null) {
            timeoutTimer?.cancel()
            timeoutTimer?.purge()
        }
        timeoutTimer = Timer()
        timeoutTimer?.schedule(10000) {
            Log.d("LoginView", "Timeout")
            loadError = true
        }

        showLoading = true
    }

    fun cancelTimeoutTimer() {
        Log.d("LoginView", "Cancel timeout timer")
        if (timeoutTimer != null) {
            timeoutTimer?.cancel()
            timeoutTimer?.purge()
        }
        loadError = false
    }

    fun loginSuccess(cookies: String) {
        LoginManager.username = username
        LoginManager.password = password
        LoginManager.cookies = cookies
        LoginManager.refreshLoginState()
        webUsername = null
        webPassword = null
        loginUrl = "about:blank"
    }

    LoginWebView(
        url = loginUrl,
        username = webUsername,
        password = webPassword,
        onPageStarted = { url, cookies ->
            loginUrl = url
            Log.d("LoginView", "Page started: $url")
            Log.d("LoginView", "Cookies: $cookies")
            if (url.startsWith(HoppiiURLs.Home.string)) {
                Log.d("LoginView", "Login success")
                loginSuccess(cookies = cookies)
            }
        },
        onFinished = {
            loginUrl = it
            Log.d("LoginView", "Finished: $it")
            if (it.startsWith(HoppiiURLs.HoseiSSO.string)) {
                cancelTimeoutTimer()
            }
            if (loadingFirstPage) {
                if (username == "" || password == "") {
                    loadingFirstPage = false
                    showLoading = false
                } else {
                    // Try to use the exist credential.
                    Log.d("LoginView", "Try to use the exist credential.")
                    webUsername = username
                    webPassword = password
                    setTimeoutTimer()
                }
            }
        },
        onError = {
            if (it == "net::ERR_FAILED") {
                return@LoginWebView
            }
            loadError = true
            showLoading = false
            Log.d("LoginView", "Load Error: $it")
        },
        onLoginError = {
//            LoginManager.username = null
//            LoginManager.password = null
            username = ""
            password = ""
            loginError = true
            showLoading = false
            Log.d("LoginView", "Login error")
        }
    )

    if (loadError) {
        NetworkErrorView {
            loadError = false
            loginError = false
            loginUrl = "about:blank"
            handler.postDelayed({
                loginUrl = HoppiiURLs.RequestLogin.string
                loadingFirstPage = true
            }, 500)
            setTimeoutTimer()
        }
    } else if (showLoading || LoginManager.loginState == null) {
        LoadingAnimationView()
    } else {
        LoginInterfaceView(
            username = username,
            password = password,
            error = loginError,
            onUsernameChange = { username = it },
            onPasswordChange = { password = it },
            loginAction = {
                // Demo account.
                if (username == "11N4514" && password == "password") {
                    LoginManager.isDemo = true
                    loginSuccess(cookies = "")
                    return@LoginInterfaceView
                }

                webUsername = username
                webPassword = password
                setTimeoutTimer()
            }
        )
    }
}