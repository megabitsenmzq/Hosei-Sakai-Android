package com.jinyumeng.hosei_sakai.views.sites_tab.detail.announcements

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import com.jinyumeng.hosei_sakai.hoppii.remote.announcements.AnnouncementItem
import com.jinyumeng.hosei_sakai.views.common.AttachmentItemView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun AnnouncementDetailView(announcement: AnnouncementItem, navController: NavController) {
    Scaffold(
        topBar = {
            AnnouncementDetailTopBarView(title = announcement.title) {
                navController.popBackStack()
            }
        }
    ) {
        Column(
            modifier = Modifier.padding(top = it.calculateTopPadding())
        ) {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        if (WebViewFeature.isFeatureSupported(WebViewFeature.ALGORITHMIC_DARKENING)) {
                            val darkMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
                            WebSettingsCompat.setAlgorithmicDarkeningAllowed(settings, darkMode)
                        }
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        webViewClient = WebViewClient()
                        settings.javaScriptEnabled = true
                        loadData(announcement.body, "text/html", "UTF-8")
                    }
                },
                modifier = Modifier.weight(1f)
            )
            LazyColumn {
                items(announcement.attachments) { attachment ->
                    AttachmentItemView(title = attachment.name, url = attachment.url)
                }
            }
        }
    }
}