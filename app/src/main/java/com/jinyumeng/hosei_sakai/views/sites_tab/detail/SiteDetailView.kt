package com.jinyumeng.hosei_sakai.views.sites_tab.detail

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jinyumeng.hosei_sakai.hoppii.AnnouncementManager
import com.jinyumeng.hosei_sakai.hoppii.ContentsManager
import com.jinyumeng.hosei_sakai.hoppii.remote.announcements.AnnouncementItem
import com.jinyumeng.hosei_sakai.hoppii.remote.contents.ContentItem
import com.jinyumeng.hosei_sakai.hoppii.remote.sites.SiteItem
import com.jinyumeng.hosei_sakai.views.common.AttachmentItemView
import com.jinyumeng.hosei_sakai.views.common.ListSectionTitleView
import com.jinyumeng.hosei_sakai.views.common.SimpleTextListRowView
import com.jinyumeng.hosei_sakai.views.sites_tab.detail.announcements.AnnouncementDetailView

@Composable
fun SiteDetailView(site: SiteItem, navController: NavController) {

    var announcements by remember { mutableStateOf(listOf<AnnouncementItem>()) }
    var contents by remember { mutableStateOf(listOf<ContentItem>()) }
    LaunchedEffect(Unit) {
        announcements = AnnouncementManager.requestAnnouncement(site.id)?.announcementCollection ?: listOf()
        contents = ContentsManager.requestContents(site.id)?.contentCollection?.drop(1) ?: listOf()
    }
    val idOfList = "site_detail"
    val siteDetailNavController = rememberNavController()
    NavHost(
        navController = siteDetailNavController,
        startDestination = idOfList
    ) {
        composable(idOfList) {
            Scaffold(
                topBar = {
                    SiteDetailTopBarView(back = {
                        navController.popBackStack()
                    })
                }
            ) { paddingValues ->
                LazyColumn(modifier = Modifier.padding(top = paddingValues.calculateTopPadding())) {
                    item {
                        ListSectionTitleView(title = "お知らせ")
                    }
                    items(announcements) {
                        SimpleTextListRowView(title = it.title) {
                            siteDetailNavController.navigate("announcement_detail/${it.id}")
                        }
                    }
                    item {
                        ListSectionTitleView(title = "教材")
                    }
                    items(contents) {
                        when(it.type) {
                            "collection" -> {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(start = 16.dp, top = 16.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Folder,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                    Text(
                                        text = it.title,
                                        style = MaterialTheme.typography.titleMedium,
                                    )
                                }
                            }
                            else -> {
                                AttachmentItemView(title = it.title, url = it.url, withBackground = false)       
                            }
                        }
                    }
                }
            }
        }
        composable("announcement_detail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            val announcement = announcements.find { it.id == id }
            if (announcement != null) {
                AnnouncementDetailView(announcement = announcement, navController = siteDetailNavController)
            } else {
                Text(text = "Error")
            }
        }

    }
}