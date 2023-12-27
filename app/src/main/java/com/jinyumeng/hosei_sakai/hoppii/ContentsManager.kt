package com.jinyumeng.hosei_sakai.hoppii

import android.util.Log
import com.jinyumeng.hosei_sakai.hoppii.common.CookieInterceptor
import com.jinyumeng.hosei_sakai.hoppii.common.DateAdapter
import com.jinyumeng.hosei_sakai.hoppii.remote.contents.Contents
import com.jinyumeng.hosei_sakai.hoppii.remote.contents.ContentsService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object ContentsManager {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .add(DateAdapter())
        .build()

    suspend fun requestContents(siteID: String): Contents? {
//        if (LoginManager.isDemo == true) {
//            return Contents(contentCollection = DemoData.demoContents.toMutableList())
//        }

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
            .create(ContentsService::class.java)

        try {
            val response = retrofit.getContents(siteID = siteID)
            if (response.isSuccessful) {
                return response.body()
            } else {
                Log.e("ContentsManager", "Network Error: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("ContentsManager", "Json Error: ${e.message}")
            LoginManager.refreshLoginState()
        }
        return null
    }
}