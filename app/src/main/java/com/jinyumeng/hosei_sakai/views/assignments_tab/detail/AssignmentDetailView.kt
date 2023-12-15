package com.jinyumeng.hosei_sakai.views.assignments_tab.detail

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import androidx.webkit.WebViewFeature.isFeatureSupported
import com.jinyumeng.hosei_sakai.hoppii.AssignmentsManager
import com.jinyumeng.hosei_sakai.hoppii.remote.assignments.AssignmentAttachment
import com.jinyumeng.hosei_sakai.hoppii.remote.assignments.AssignmentItem
import com.jinyumeng.hosei_sakai.views.common.AttachmentItemView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun AssignmentDetailView(assignment: AssignmentItem, navController: NavController) {
    var attachments by remember { mutableStateOf(null as List<AssignmentAttachment>?) }

    LaunchedEffect(Unit) {
        Log.d("AssignmentDetailView", assignment.id)
        val newAttachments = AssignmentsManager.downloadAttachments(assignment.id)
        attachments = newAttachments
    }
    Scaffold(
        topBar = {
            AssignmentDetailTopBarView(back = {
                navController.popBackStack()
            })
        }
    ) {
        Column(
            modifier = Modifier.padding(top = it.calculateTopPadding())
        ) {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        if (isFeatureSupported(WebViewFeature.ALGORITHMIC_DARKENING)) {
                            WebSettingsCompat.setAlgorithmicDarkeningAllowed(settings, true)
                        }
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        webViewClient = WebViewClient()
                        settings.javaScriptEnabled = true
                        loadData(assignment.instructions, "text/html", "UTF-8")
                    }
                },
                modifier = Modifier.weight(1f)
            )
            if (attachments != null) {
                LazyColumn {
                    items(attachments!!) { attachment ->
                        AttachmentItemView(title = attachment.title, url = attachment.url)
                    }
                }
            }
        }
    }
}