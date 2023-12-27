package com.jinyumeng.hosei_sakai.hoppii

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.webkit.CookieManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.jinyumeng.hosei_sakai.hoppii.common.CookieInterceptor
import com.jinyumeng.hosei_sakai.hoppii.remote.user_info.UserInfo
import com.jinyumeng.hosei_sakai.hoppii.remote.user_info.UserInfoService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


object LoginManager {

    var loginState by mutableStateOf(null as Boolean?)
        private set
    var currentUser by mutableStateOf(null as UserInfo?)
        private set

    var isDemo by mutableStateOf(null as Boolean?)

    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    var sharedPreferences: SharedPreferences? = null

    var username: String?
        get() {
            return sharedPreferences?.getString("username", "")
        }
        set(value) {
            if (value == null) {
                sharedPreferences?.edit()?.remove("username")?.apply()
                return
            }
            sharedPreferences?.edit()?.putString("username", value)?.apply()
        }

    var password: String?
        get() {
            return sharedPreferences?.getString("password", "")
        }
        set(value) {
            if (value == null) {
                sharedPreferences?.edit()?.remove("password")?.apply()
                return
            }
            sharedPreferences?.edit()?.putString("password", value)?.apply()
        }

    var cookies: String?
        get() {
            return sharedPreferences?.getString("cookies", "")
        }
        set(value) {
            if (value == null) {
                sharedPreferences?.edit()?.remove("cookies")?.apply()
                return
            }
            sharedPreferences?.edit()?.putString("cookies", value)?.apply()
        }

    fun setup(context: Context) {
        val prefFileName = "login"

        val mainKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        sharedPreferences = EncryptedSharedPreferences.create(
            context,
            prefFileName,
            mainKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        if (username == "11N4514" && password == "password") {
            isDemo = true
        }

        refreshLoginState()
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun refreshLoginState() {
        if (isDemo == true) {
            loginState = true
            currentUser = DemoData.demoUser
            Log.d("LoginManager", "Logged in as demo user")
            return
        }
        Log.d("LoginManager", "Refresh Login State")
        if (loginState == false) {
            loginState = null
        }
        GlobalScope.launch {
            val userInfo = requestCurrentUser()
            if (userInfo != null) {
                loginState = true
                currentUser = userInfo
                Log.d("LoginManager", "Logged in as ${userInfo.displayId}")
                return@launch
            }
            Log.d("LoginManager", "Not logged in")
            loginState = false
            currentUser = null
        }

    }

    fun clearCookies() {
        loginState = null
        currentUser = null
        cookies = null
        CookieManager.getInstance().removeAllCookies(null)
    }

    fun logout() {
        username = null
        password = null
        clearCookies()
    }

    private suspend fun requestCurrentUser(): UserInfo? {
        val interceptor = CookieInterceptor(cookies = cookies)

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(HoppiiURLs.Root.string)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(httpClient)
            .build()
            .create(UserInfoService::class.java)

        try {
            val response = retrofit.getCurrentUser()
            if (response.isSuccessful) {
                return response.body()
            } else {
                Log.e("LoginManager", "Network Error: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
           Log.e("LoginManager", "Json Error: ${e.message}")
        }
        return null
    }
    suspend fun requestUserInfo(userID: String): UserInfo? {
        val interceptor = CookieInterceptor(cookies = cookies)

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(HoppiiURLs.Root.string)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(httpClient)
            .build()
            .create(UserInfoService::class.java)

        try {
            val response = retrofit.getUser(userID = userID)
            if (response.isSuccessful) {
                return response.body()
            } else {
                Log.e("LoginManager", "Network Error: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("LoginManager", "Json Error: ${e.message}")
        }
        return null
    }


}