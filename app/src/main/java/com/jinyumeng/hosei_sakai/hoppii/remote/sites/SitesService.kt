package com.jinyumeng.hosei_sakai.hoppii.remote.sites

import retrofit2.Response
import retrofit2.http.GET

interface SitesService {
    @GET("direct/site.json?_limit=200")
    suspend fun getSites(): Response<Sites>
}