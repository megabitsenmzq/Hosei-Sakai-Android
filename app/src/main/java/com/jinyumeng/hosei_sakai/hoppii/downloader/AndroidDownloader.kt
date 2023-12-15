package com.jinyumeng.hosei_sakai.hoppii.downloader

import android.app.DownloadManager
import android.content.Context
import androidx.core.net.toUri
import com.jinyumeng.hosei_sakai.hoppii.LoginManager

class AndroidDownloader(
    context: Context
): Downloader {

    private val downloadManager = context.getSystemService(DownloadManager::class.java)

    override fun downloadFile(url: String): Long {
        val request = DownloadManager.Request(url.toUri())
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .addRequestHeader("Cookie", LoginManager.cookies)
        return downloadManager.enqueue(request)
    }
}