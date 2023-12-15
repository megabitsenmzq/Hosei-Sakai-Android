package com.jinyumeng.hosei_sakai.views.sites_tab

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jinyumeng.hosei_sakai.hoppii.SitesManager
import com.jinyumeng.hosei_sakai.views.common.LoadingAnimationView
import com.jinyumeng.hosei_sakai.views.sites_tab.detail.SiteDetailView
import com.jinyumeng.hosei_sakai.views.sites_tab.list.SiteListView

@Composable
fun SitesView() {
    LaunchedEffect(Unit) {
        SitesManager.refreshSites()
    }
    val idOfList = "site_list"
    val navController = rememberNavController()
    val siteList = SitesManager.sites
    if (siteList != null) {
        NavHost(
            navController = navController,
            startDestination = idOfList
        ) {
            composable(idOfList) {
                SiteListView(siteList = siteList, navController = navController)
            }
            composable("site_detail/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")
                val site = siteList.find { it.id == id }
                if (site != null) {
                    SiteDetailView(site = site, navController = navController)
                } else {
                    Text(text = "Error")
                }
            }

        }
    } else {
        LoadingAnimationView()
    }
}