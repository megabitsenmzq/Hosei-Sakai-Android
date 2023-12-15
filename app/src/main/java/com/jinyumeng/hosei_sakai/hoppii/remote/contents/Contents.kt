package com.jinyumeng.hosei_sakai.hoppii.remote.contents

import com.squareup.moshi.Json

data class Contents(
    @Json(name = "content_collection")
    val contentCollection: List<ContentItem>
)
