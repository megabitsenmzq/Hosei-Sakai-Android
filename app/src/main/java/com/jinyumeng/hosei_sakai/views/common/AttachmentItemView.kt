package com.jinyumeng.hosei_sakai.views.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jinyumeng.hosei_sakai.hoppii.downloader.AndroidDownloader

@Composable
fun AttachmentItemView(
    title: String,
    url: String,
    modifier: Modifier = Modifier,
    withBackground: Boolean = true
) {
    val downloader = AndroidDownloader(context = LocalContext.current)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = if (withBackground) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.background),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            modifier = Modifier
                .padding(start = 16.dp, end = 8.dp, top = 16.dp, bottom = 16.dp)
                .weight(1f)
        )
        IconButton(onClick = {
            downloader.downloadFile(url)
        }, modifier = Modifier
            .padding(end = 6.dp)
        ) {
            Icon(imageVector = Icons.Default.Download, contentDescription = "表示")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AttachmentItemViewPreview() {
    Column {
        AttachmentItemView(
            title = "課題資料.pdf",
            url = "https://www.google.com"
        )
        AttachmentItemView(
            title = "課題資料.pdf",
            url = "https://www.google.com",
            withBackground = false
        )
    }
}