package com.jinyumeng.hosei_sakai.hoppii

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

object TimetableManager {
    fun <T> transpose(list: List<List<T>>): List<List<T>> =
        List(list.first().size) { index ->
            list.map { row -> row[index] }
        }

    suspend fun getTimetable(): List<List<String>>? = withContext(Dispatchers.IO) {
        if (LoginManager.isDemo == true) {
            return@withContext DemoData.demoTimetable
        }

        val url = getTimetablePageURL() ?: return@withContext null
        val newTimeTable = getTimetableList(url) ?: return@withContext null
        return@withContext transpose(newTimeTable)
    }
    suspend fun getTimetablePageURL(): String? = withContext(
        Dispatchers.IO) {
        val cookieString = LoginManager.cookies
        if (cookieString == null) {
            Log.e("TimetableManager", "No cookies while downloading timetable")
            return@withContext null
        }

        val homePage = HoppiiURLs.Home.string
        val cookies = cookieString.split(";").associate {
            val (key, value) = it.split("=")
            key to value
        }

        val doc = Jsoup.connect(homePage)
            .cookies(cookies)
            .timeout(60000)
            .get()

        val attachmentList = doc.getElementsByClass("Mrphs-toolBody--sakai-timetable")
        val iframe = attachmentList.select("iframe").first()
        val url = iframe?.attr("src")

        Log.d("TimetableManager", "Timetable URL: $url")

        return@withContext url
    }

    suspend fun getTimetableList(url: String) : List<List<String>>? = withContext(
        Dispatchers.IO) {

        val cookieString = LoginManager.cookies
        if (cookieString == null) {
            Log.e("TimetableManager", "No cookies while downloading timetable")
            return@withContext null
        }

        val cookies = cookieString.split(";").associate {
            val (key, value) = it.split("=")
            key to value
        }

        val doc = Jsoup.connect(url)
            .cookies(cookies)
            .timeout(60000)
            .get()

        val rows = doc.select("tr")
        if (rows.isEmpty()) {
            Log.e("TimetableManager", "No rows in timetable.")
            return@withContext null
        }
        val table = rows.map { row ->
            row.select("td")
                .drop(1) // Drop headers.
                .map { it.select("a").attr("title") ?: "" }
        }
        if (table.isNotEmpty()) {
            return@withContext table.drop(1) // Drop headers.
        } else {
            return@withContext null
        }
    }
}