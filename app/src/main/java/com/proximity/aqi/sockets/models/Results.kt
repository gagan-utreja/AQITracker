package com.proximity.aqi.sockets.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Results(
    @Json(name = "city") val city: String,
    @Json(name = "aqi") val aqi: Float,
)
