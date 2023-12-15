package com.jinyumeng.hosei_sakai.views.settings_tab

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import com.jinyumeng.hosei_sakai.BuildConfig
import com.jinyumeng.hosei_sakai.hoppii.AssignmentsManager
import com.jinyumeng.hosei_sakai.hoppii.LoginManager
import com.jinyumeng.hosei_sakai.views.common.ListSectionTitleView

@Composable
fun SettingsView(modifier: Modifier = Modifier) {
    val clipboard = LocalClipboardManager.current
    var openDialog by remember { mutableStateOf(false) }


    Column(modifier = modifier) {
        ListSectionTitleView(title = "ユーザー情報")
        ListItem(
            headlineContent = { Text("学籍番号") },
            trailingContent = {
                Text(
                    text = LoginManager.currentUser?.displayId ?: "",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        )
        ListItem(
            headlineContent = { Text("名前") },
            trailingContent = {
                Text(
                    text = LoginManager.currentUser?.displayName ?: "",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        )
        ListItem(
            headlineContent = { Text("メール") },
            trailingContent = {
                Text(
                    text = LoginManager.currentUser?.email ?: "",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            modifier = modifier.clickable {
                clipboard.setText(AnnotatedString(LoginManager.currentUser?.email ?: ""))
                openDialog = true
            }
        )
        ListItem(
            headlineContent = {
                Text("ログアウト", color = MaterialTheme.colorScheme.error)
            },
            modifier = modifier.clickable {
                LoginManager.logout()
            }
        )
        ListSectionTitleView(title = "表示")
        ListItem(
            headlineContent = { Text("安全な締切を表示") },
            trailingContent = {
                Switch(checked = AssignmentsManager.showOptimizedDate, onCheckedChange = {
                    AssignmentsManager.savedShowOptimizedDate = it
                })
            }
        )
        if (BuildConfig.DEBUG) {
            ListSectionTitleView(title = "Debug")
            ListItem(
                headlineContent = {
                    Text("Clear Cookies", color = MaterialTheme.colorScheme.error)
                },
                modifier = modifier.clickable {
                    LoginManager.clearCookies()
                }
            )
        }
    }

    if (openDialog) {
        AlertDialog(
            onDismissRequest = {   },
            text = {
                Text(text = "メールをコピーしました。")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = null
        )
    }

}

@Preview(showBackground = true)
@Composable
fun SettingsViewPreview() {
    SettingsView()
}