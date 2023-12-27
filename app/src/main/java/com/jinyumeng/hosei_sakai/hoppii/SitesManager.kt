package com.jinyumeng.hosei_sakai.hoppii

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.compose.runtime.setValue
import com.jinyumeng.hosei_sakai.hoppii.common.CookieInterceptor
import com.jinyumeng.hosei_sakai.hoppii.common.DateAdapter
import com.jinyumeng.hosei_sakai.hoppii.remote.sites.Sites
import com.jinyumeng.hosei_sakai.hoppii.remote.sites.SitesService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object SitesManager {
    val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var refreshJob: Job? = null
    var refreshing by mutableStateOf(null as Boolean?)
        private set

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .add(DateAdapter())
        .build()

    var sites by mutableStateOf(savedSites?.siteCollection?.toList(), neverEqualPolicy())
        private set

    private var savedSites: Sites?
        get() {
            val jsonAdapter = moshi.adapter(Sites::class.java)
            val jsonString = LoginManager.sharedPreferences?.getString("savedSites", "")
            if (jsonString == null || jsonString == "") {
                return null
            }
            return jsonAdapter.fromJson(jsonString)
        }
        set(value) {
            if (value == null) {
                LoginManager.sharedPreferences?.edit()?.remove("savedSites")?.apply()
                return
            }
            val jsonAdapter = moshi.adapter(Sites::class.java)
            LoginManager.sharedPreferences?.edit()?.putString("savedSites", jsonAdapter.toJson(value))?.apply()
        }

    @OptIn(DelicateCoroutinesApi::class)
    fun refreshSites() {
        refreshJob?.cancel()
        refreshing = true

        if (LoginManager.isDemo == true) {
            sites = DemoData.demoSites.toMutableList()
            savedSites = Sites(siteCollection = DemoData.demoSites.toMutableList())
            refreshing = false
            return
        }

        Log.d("SitesManager", "Refresh Sites")
        refreshJob = coroutineScope.launch {
            val newSites = requestSites()

            if (newSites == null) {
                refreshing = false
                return@launch
            }

            savedSites = newSites
            sites = newSites.siteCollection
            refreshing = false
            Log.d("SitesManager", "Sites Refreshed")
        }
    }

    private suspend fun requestSites(): Sites? {
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
            .create(SitesService::class.java)

        try {
            val response = retrofit.getSites()
            if (response.isSuccessful) {
                return response.body()
            } else {
                Log.e("SitesManager", "Network Error: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("SitesManager", "Json Error: ${e.message}")
            LoginManager.refreshLoginState()
        }
        return null
    }
}