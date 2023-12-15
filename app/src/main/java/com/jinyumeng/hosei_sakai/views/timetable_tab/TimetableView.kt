package com.jinyumeng.hosei_sakai.views.timetable_tab

import android.annotation.SuppressLint
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import androidx.webkit.WebViewFeature.isFeatureSupported
import com.jinyumeng.hosei_sakai.hoppii.HoppiiURLs
import com.jinyumeng.hosei_sakai.hoppii.LoginManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun TimetableView(modifier: Modifier = Modifier) {
    var url by remember { mutableStateOf(null as String?) }

    suspend fun getTimetableURL(): String? = withContext(Dispatchers.IO) {
        val cookieString = LoginManager.cookies
        if (cookieString == null) {
            Log.e("TimetableView", "No cookies while getting timetable URL")
            return@withContext null
        }

        val cookies = cookieString.split(";").associate {
            val (key, value) = it.split("=")
            key to value
        }
        val doc = Jsoup.connect(HoppiiURLs.Home.string)
            .cookies(cookies)
            .timeout(60000)
            .get()

        val attachmentList = doc.getElementsByClass("Mrphs-toolBody--sakai-timetable")
        val iframe = attachmentList.select("iframe").first()
        val timetableURL = iframe?.attr("src")
        Log.d("TimetableView", "Timetable URL: $timetableURL")
        return@withContext timetableURL
    }

    LaunchedEffect(Unit) {
        url = getTimetableURL()
    }

    AndroidView(factory = { context ->
        WebView(context).apply {
            if (isFeatureSupported(WebViewFeature.ALGORITHMIC_DARKENING)) {
                WebSettingsCompat.setAlgorithmicDarkeningAllowed(settings, true)
            }
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            url?.let { loadUrl(it) }
        }
    }, update = {
        url?.let { it1 -> it.loadUrl(it1) }
    }, modifier = modifier)

}