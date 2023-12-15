package com.jinyumeng.hosei_sakai.views.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jinyumeng.hosei_sakai.ui.theme.HoseiSakaiTheme

@Composable
fun NetworkErrorView(retryAction: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                Icons.Rounded.Warning,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 10.dp)
            )
            Text(
                text = "Hoppii に接続できません",
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 35.dp)
            )
            Button(onClick = retryAction) {
                Text("再試行")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NetworkErrorViewPreview() {
    HoseiSakaiTheme {
        NetworkErrorView {}
    }
}