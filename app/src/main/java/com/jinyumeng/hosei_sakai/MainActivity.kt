package com.jinyumeng.hosei_sakai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jinyumeng.hosei_sakai.hoppii.LoginManager
import com.jinyumeng.hosei_sakai.ui.theme.HoseiSakaiTheme
import com.jinyumeng.hosei_sakai.views.ContentView
import com.jinyumeng.hosei_sakai.views.login.LoginView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Allow imePadding to works.
        WindowCompat.setDecorFitsSystemWindows(window, false)

        LoginManager.setup(context = this.applicationContext)

        setContent {
            val systemUiController = rememberSystemUiController()
            val isDark = isSystemInDarkTheme()
            SideEffect {
                systemUiController.setSystemBarsColor(
                    color = Color.Transparent,
                    darkIcons = !isDark,
                )
            }
            HoseiSakaiTheme {
                if (LoginManager.loginState == true) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        ContentView()
                    }
                } else {
                    LoginView()
                }
            }
        }
    }
}
