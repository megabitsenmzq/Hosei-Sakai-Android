package com.jinyumeng.hosei_sakai.hoppii.remote.sites

import com.squareup.moshi.Json

data class Sites(
    @Json(name = "site_collection")
    var siteCollection: MutableList<SiteItem>
)
