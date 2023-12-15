package com.jinyumeng.hosei_sakai.views.assignments_tab.detail

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.jinyumeng.hosei_sakai.ui.theme.HoseiSakaiTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignmentDetailTopBarView(back: () -> Unit) {
    TopAppBar(
        title = { Text("課題情報") },
        navigationIcon = {
            IconButton(onClick = back) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "戻す"
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun AssignmentDetailTopBarViewPreview() {
    HoseiSakaiTheme {
        AssignmentDetailTopBarView(back = {})
    }
}