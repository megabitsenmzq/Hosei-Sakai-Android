package com.jinyumeng.hosei_sakai.views.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jinyumeng.hosei_sakai.ui.theme.HoseiSakaiTheme

@Composable
fun SimpleTextListRowView(
    title: String,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(title) },
        trailingContent = {
            Icon(
                imageVector = Icons.Default.ArrowRight,
                contentDescription = "開く"
            )
        },
        modifier = Modifier.clickable {
            onClick()
        }
    )
//    Divider()
}

@Preview(showBackground = true)
@Composable
fun SiteListRowViewPreview() {
    HoseiSakaiTheme {
        Column {
            SimpleTextListRowView(title = "Title") {}
            SimpleTextListRowView(title = "Title") {}
            SimpleTextListRowView(title = "Title") {}
        }
    }
}