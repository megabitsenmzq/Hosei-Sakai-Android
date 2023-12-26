package com.jinyumeng.hosei_sakai.hoppii

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.compose.runtime.setValue
import com.jinyumeng.hosei_sakai.hoppii.common.CookieInterceptor
import com.jinyumeng.hosei_sakai.hoppii.common.DateAdapter
import com.jinyumeng.hosei_sakai.hoppii.remote.assignments.AssignmentAttachment
import com.jinyumeng.hosei_sakai.hoppii.remote.assignments.AssignmentItem
import com.jinyumeng.hosei_sakai.hoppii.remote.assignments.Assignments
import com.jinyumeng.hosei_sakai.hoppii.remote.assignments.AssignmentsService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import org.jsoup.Jsoup
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object AssignmentsManager {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var refreshJob: Job? = null
    var refreshing by mutableStateOf(null as Boolean?)
        private set

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .add(DateAdapter())
        .build()

    var assignments by mutableStateOf(savedAssignments?.assignmentCollection?.toList(), neverEqualPolicy())
        private set

    private var savedAssignments: Assignments?
        get() {
            val jsonAdapter = moshi.adapter(Assignments::class.java)
            val jsonString = LoginManager.sharedPreferences?.getString("savedAssignments", "")
            if (jsonString == null || jsonString == "") {
                return null
            }
            return jsonAdapter.fromJson(jsonString)
        }
        set(value) {
            if (value == null) {
                LoginManager.sharedPreferences?.edit()?.remove("savedAssignments")?.apply()
                return
            }
            val jsonAdapter = moshi.adapter(Assignments::class.java)
            LoginManager.sharedPreferences?.edit()?.putString("savedAssignments", jsonAdapter.toJson(value))?.apply()
        }

    fun updateAssignments(assignments: MutableList<AssignmentItem>) {
        this.assignments = assignments
        savedAssignments = Assignments(assignments)
    }

    var finishGestureTipShowed by mutableStateOf(savedFinishGestureTipShowed ?: false)
        private set

    var savedFinishGestureTipShowed: Boolean?
        get() {
            return LoginManager.sharedPreferences?.getBoolean("finishGestureTipShowed", false)
        }
        set(value) {
            if (value == null) {
                LoginManager.sharedPreferences?.edit()?.remove("finishGestureTipShowed")?.apply()
                return
            }
            finishGestureTipShowed = value
            LoginManager.sharedPreferences?.edit()?.putBoolean("finishGestureTipShowed", value)?.apply()
        }

    var showOptimizedDate by mutableStateOf(savedShowOptimizedDate ?: true)
        private set

    var savedShowOptimizedDate: Boolean?
        get() {
            return LoginManager.sharedPreferences?.getBoolean("showOptimizedDate", true)
        }
        set(value) {
            if (value == null) {
                LoginManager.sharedPreferences?.edit()?.remove("showOptimizedDate")?.apply()
                return
            }
            showOptimizedDate = value
            LoginManager.sharedPreferences?.edit()?.putBoolean("showOptimizedDate", value)?.apply()
        }


    private var teacherNames: Map<String, String>
        get() {
            val jsonAdapter = moshi.adapter(Map::class.java)
            val jsonString = LoginManager.sharedPreferences?.getString("teacherNames", "")
            if (jsonString == null || jsonString == "") {
                Log.d("AssignmentsManager", "No Saved Name")
                return mutableMapOf()
            }
            val newValue = jsonAdapter.fromJson(jsonString)?.entries?.associate { (key, value) ->
                key.toString() to value.toString()
            } ?: return mutableMapOf()
            return newValue.toMutableMap()
        }
        set(value) {
            val jsonAdapter = moshi.adapter(Map::class.java)
            val json = jsonAdapter.toJson(value)
            LoginManager.sharedPreferences?.edit()?.putString("teacherNames", json)?.apply()
        }

    fun refreshAssignments() {
        refreshJob?.cancel()
        refreshing = true
        Log.d("AssignmentsManager", "Refresh Assignments")

        if (LoginManager.isDemo == true) {
            updateAssignments(DemoData.demoAssignments.toMutableList())
            refreshing = false
            return
        }

        refreshJob = coroutineScope.launch {
            val newAssignments = requestAssignments()

            if (newAssignments == null) {
                refreshing = false
                return@launch
            }

            val openAssignments = newAssignments.assignmentCollection.filter { it.status == "OPEN" }
            val sortedAssignments = openAssignments.sortedBy { it.dueTimeString }

            val newNames = teacherNames.toMutableMap()
            // Find teacher names
            for (assignment in sortedAssignments) {
                val savedName = teacherNames[assignment.author]
                if (savedName != null) {
                    assignment.authorName = savedName
                    continue
                }


                val userInfo = LoginManager.requestUserInfo(assignment.author)
                if (userInfo != null) {
                    assignment.authorName = userInfo.displayName
                    newNames[assignment.author] = userInfo.displayName
                }
            }
            teacherNames = newNames

            // Set Finished
            for (assignment in sortedAssignments) {
                val oldAssignment = savedAssignments?.assignmentCollection?.find { it.id == assignment.id }
                if (oldAssignment != null) {
                    assignment.markAsFinished = oldAssignment.markAsFinished
                }
            }

            newAssignments.assignmentCollection = sortedAssignments.toMutableList()
            updateAssignments(newAssignments.assignmentCollection)
            refreshing = false
            Log.d("AssignmentsManager", "Assignments Refreshed")
        }
    }

    private suspend fun requestAssignments(): Assignments? {
        val interceptor = CookieInterceptor(cookies = LoginManager.cookies)

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(HoppiiURLs.Root.string)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(httpClient)
            .build()
            .create(AssignmentsService::class.java)

        try {
            val response = retrofit.getAssignments()
            if (response.isSuccessful) {
                return response.body()
            } else {
                Log.d("AssignmentsManager", "Json Decode Error: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.d("AssignmentsManager", "Other Error: ${e.message}")
            LoginManager.refreshLoginState()
        }
        return null
    }

    suspend fun downloadAttachments(id: String): List<AssignmentAttachment>? = withContext(Dispatchers.IO) {
        if (LoginManager.isDemo == true) {
            return@withContext null
        }

        val cookieString = LoginManager.cookies
        if (cookieString == null) {
            Log.e("AssignmentsManager", "No cookies while downloading attachment list")
            return@withContext null
        }

        val assignmentPage = HoppiiURLs.AssignmentDetail.with(id)
        val cookies = cookieString.split(";").associate {
            val (key, value) = it.split("=")
            key to value
        }
        val doc = Jsoup.connect(assignmentPage)
            .cookies(cookies)
            .timeout(60000)
            .get()

        val attachmentList = doc.getElementsByClass("attachList")
        val items = attachmentList.select("li")
            .mapNotNull {
                val link = it.select("a").first()
                if (link == null) {
                    Log.d("AssignmentsManager", "No Link")
                    return@mapNotNull null
                }
                val url = link.attr("href")
                val title = link.text()
                AssignmentAttachment(title, url)
            }

        Log.d("AssignmentsManager", "Attachment List: $items")

        return@withContext items
    }
}