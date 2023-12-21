package com.jinyumeng.hosei_sakai.views.timetable_tab

import android.annotation.SuppressLint
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import androidx.webkit.WebViewFeature.isFeatureSupported
import com.jinyumeng.hosei_sakai.hoppii.AssignmentsManager
import com.jinyumeng.hosei_sakai.hoppii.HoppiiURLs
import com.jinyumeng.hosei_sakai.hoppii.LoginManager
import com.jinyumeng.hosei_sakai.hoppii.TimetableManager
import com.jinyumeng.hosei_sakai.views.common.LoadingAnimationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun TimetableView(modifier: Modifier = Modifier) {
    var table by remember { mutableStateOf(null as List<List<String>>?) }
    var noSunday by remember { mutableStateOf(false) }
    var noSaturday by remember { mutableStateOf(false) }

    fun <T> transpose(list: List<List<T>>): List<List<T>> =
        list.first().mapIndexed { index, _ ->
            list.map { row -> row[index] }
        }

    LaunchedEffect(Unit) {
        val newTable = TimetableManager.getTimetable()
        newTable?.let {
            var timetable = transpose(it)

            if (timetable.isEmpty()) {
                Log.e("TimetableView", "Failed to get timetable.")
                return@let
            }

            val sunItem = timetable.first().reduce(String::plus)
            if (sunItem.isEmpty()) {
                noSunday = true
                timetable = timetable.drop(1)
            }
            val satItem = timetable.last().reduce(String::plus)
            if (satItem.isEmpty()) {
                noSaturday = true
                timetable = timetable.dropLast(1)
            }
            table = timetable
        } ?: run {
            Log.e("TimetableView", "Failed to get timetable.")
        }
    }

    table?.let {
        Surface(modifier = modifier) {
            TimetableContentView(noSunday = noSunday, noSaturday = noSaturday, table = it)
        }
    } ?: run {
        LoadingAnimationView()
    }

}