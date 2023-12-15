package com.jinyumeng.hosei_sakai.views

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jinyumeng.hosei_sakai.ui.theme.HoseiSakaiTheme
import com.jinyumeng.hosei_sakai.views.assignments_tab.AssignmentsView
import com.jinyumeng.hosei_sakai.views.settings_tab.SettingsView
import com.jinyumeng.hosei_sakai.views.sites_tab.SitesView
import com.jinyumeng.hosei_sakai.views.timetable_tab.TimetableView

@Composable
fun TabBar(
    currentTab: Int = 0,
    onTabChange: (Int) -> Unit = {}
) {
    NavigationBar(
        content = {
            NavigationBarItem(
                selected = (currentTab == 0),
                onClick = { onTabChange(0) },
                icon = {
                    Icon(Icons.Default.Create, contentDescription = null)
                },
                label = { Text("課題")}
            )
            NavigationBarItem(
                selected = (currentTab == 1),
                onClick = { onTabChange(1) },
                icon = {
                    Icon(Icons.Default.LibraryBooks, contentDescription = null)
                },
                label = { Text("授業")}
            )
            NavigationBarItem(
                selected = (currentTab == 2),
                onClick = { onTabChange(2) },
                icon = {
                    Icon(Icons.Default.CalendarMonth, contentDescription = null)
                },
                label = { Text("時間割")}
            )
            NavigationBarItem(
                selected = (currentTab == 3),
                onClick = { onTabChange(3) },
                icon = {
                    Icon(Icons.Default.Settings, contentDescription = null)
                },
                label = { Text("設定")}
            )
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ContentView() {
    var currentTab by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            TabBar(
                currentTab = currentTab,
                onTabChange = { currentTab = it }
            )
        }
    ) {
        Box(modifier = Modifier.padding(bottom = it.calculateBottomPadding())) {
            when (currentTab) {
                0 -> AssignmentsView()
                1 -> SitesView()
                2 -> TimetableView(modifier = Modifier.statusBarsPadding())
                3 -> SettingsView(modifier = Modifier.statusBarsPadding())
                else -> {
                    Text("Error: Invalid Tab Index")
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun ContentViewPreview() {
    HoseiSakaiTheme {
        ContentView()
    }
}