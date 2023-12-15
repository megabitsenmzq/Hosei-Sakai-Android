package com.jinyumeng.hosei_sakai.hoppii.common

import android.util.Log
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


// moshi date adapter
class DateAdapter {
    @ToJson
    fun toJson(date: Date): String {
        val isoDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        isoDateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return isoDateFormat.format(date)
    }

    @FromJson
    fun fromJson(json: String): Date? {
        val isoDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        isoDateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return try {
            isoDateFormat.parse(json)
        } catch (e: ParseException) {
            Log.d("DateAdapter", "fromJson: $json ${e.message}")
            null
        }
    }
}