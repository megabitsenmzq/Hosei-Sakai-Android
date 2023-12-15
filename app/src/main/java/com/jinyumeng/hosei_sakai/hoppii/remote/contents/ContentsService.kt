package com.jinyumeng.hosei_sakai.hoppii.remote.contents

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ContentsService {
    @GET("direct/content/site/{siteID}.json")
    suspend fun getContents(
        @Path("siteID") siteID: String
    ): Response<Contents>
}