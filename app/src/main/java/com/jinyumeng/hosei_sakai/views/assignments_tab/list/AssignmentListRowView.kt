package com.jinyumeng.hosei_sakai.views.assignments_tab.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.jinyumeng.hosei_sakai.hoppii.AssignmentsManager
import com.jinyumeng.hosei_sakai.ui.theme.HoseiSakaiTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun isDateSafe(date: Date): Boolean {
    if (AssignmentsManager.showOptimizedDate) {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"))
        calendar.time = date
        return calendar.get(Calendar.HOUR_OF_DAY) >= 23
    }
    return true
}
fun calcSafeDue(date: Date): Date {
    if (AssignmentsManager.showOptimizedDate) {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo")).apply {
            time = date
            if (get(Calendar.HOUR_OF_DAY) < 23) {
                add(Calendar.DATE, -1)
                set(Calendar.HOUR_OF_DAY, 23)
                set(Calendar.MINUTE, 55)
            }
        }
        return calendar.time
    }
    return date
}

@Composable
fun AssignmentListRowView(
    title: String,
    teacher: String,
    dueTime: Date,
    shouldDim: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
    formatter.timeZone = TimeZone.getTimeZone("Asia/Tokyo")
    val due = formatter.format(calcSafeDue(dueTime))
    val timeColor =  if (isDateSafe(dueTime)) Color.Unspecified else MaterialTheme.colorScheme.primary
    ListItem(
        headlineContent = {
            Text(
                text = title,
                color = if (shouldDim) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) else MaterialTheme.colorScheme.onSurface
            )
        },
        overlineContent = {
            Text(
                text = teacher,
                color = if (shouldDim) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) else MaterialTheme.colorScheme.onSurface
            )
        },
        supportingContent = {
            Text(
                text = due,
                color = if (shouldDim) timeColor.copy(alpha = 0.5f) else timeColor
            )
        },
        trailingContent = {
            Icon(
                imageVector = Icons.Default.ArrowRight,
                contentDescription = "リフレッシュ"
            )
        },
        modifier = modifier.clickable {
            onClick()
        },
    )
}

@Preview(showBackground = true)
@Composable
fun AssignmentListRowViewPreview() {
    HoseiSakaiTheme {
        Column {
            AssignmentListRowView(
                title = "課題タイトル",
                teacher = "教師名",
                dueTime = Date(),
                shouldDim = false,
                onClick = {}
            )
            AssignmentListRowView(
                title = "課題タイトル",
                teacher = "教師名",
                dueTime = Date(),
                shouldDim = true,
                onClick = {}
            )
        }
    }
}