package com.jinyumeng.hosei_sakai.views.assignments_tab.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jinyumeng.hosei_sakai.ui.theme.HoseiSakaiTheme

@Composable
fun FinishTipView() {
    Box(
        Modifier
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxWidth()
    ) {
        Row {
            Text(
                text = "右にスワイプして完成する",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp),
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "完成",
                modifier = Modifier.padding(16.dp),
                tint = MaterialTheme.colorScheme.onPrimary
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun FinishTipViewPreview() {
    HoseiSakaiTheme {
        FinishTipView()
    }
}