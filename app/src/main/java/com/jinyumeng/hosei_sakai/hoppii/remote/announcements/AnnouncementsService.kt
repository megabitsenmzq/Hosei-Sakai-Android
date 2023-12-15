package com.jinyumeng.hosei_sakai.hoppii.remote.announcements

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AnnouncementsService {
    @GET("direct/announcement/site/{siteID}.json")
    suspend fun getAnnouncements(
        @Path("siteID") siteID: String
    ): Response<Announcements>
}