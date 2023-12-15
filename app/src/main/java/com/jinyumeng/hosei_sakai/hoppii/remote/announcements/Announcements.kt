package com.jinyumeng.hosei_sakai.hoppii.remote.announcements

import com.squareup.moshi.Json

data class Announcements(
    @Json(name = "announcement_collection")
    val announcementCollection: List<AnnouncementItem>
)
