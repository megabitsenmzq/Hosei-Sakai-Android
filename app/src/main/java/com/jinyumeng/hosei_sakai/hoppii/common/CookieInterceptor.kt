package com.jinyumeng.hosei_sakai.hoppii.common

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class CookieInterceptor(cookies: String?) : Interceptor {

    private var cookies: String? = null
    init {
        this.cookies = cookies
    }
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()
        if (cookies != null) {
            request = request.newBuilder()
                .header("Cookie", cookies!!)
                .build()
        }
        return chain.proceed(request)
    }
}