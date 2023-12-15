package com.jinyumeng.hosei_sakai.views.sites_tab.list

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.jinyumeng.hosei_sakai.hoppii.remote.sites.SiteItem
import com.jinyumeng.hosei_sakai.views.common.SimpleTextListRowView

@Composable
fun SiteListView(siteList: List<SiteItem>, navController: NavController) {
    Scaffold(
        topBar = {
            SitesTopBarView()
        }
    ) {
        LazyColumn(modifier = Modifier.padding(top = it.calculateTopPadding())) {
            items(siteList) { item ->
                if (item.joinable) {
                    SimpleTextListRowView(
                        title = item.title,
                        onClick = {
                            navController.navigate("site_detail/${item.id}")
                        }
                    )
                }
            }
        }
    }
}