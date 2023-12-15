package com.jinyumeng.hosei_sakai.views.sites_tab.list

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jinyumeng.hosei_sakai.hoppii.AssignmentsManager
import com.jinyumeng.hosei_sakai.hoppii.SitesManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SitesTopBarView() {
    TopAppBar(title = { Text("授業") }, actions = {
        IconButton(onClick = {
            SitesManager.refreshSites()
        }) {
            if (AssignmentsManager.refreshing == true) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .progressSemantics()
                        .size(22.dp),
                    strokeWidth = 3.dp
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "リフレッシュ"
                )
            }
        }
    })
}