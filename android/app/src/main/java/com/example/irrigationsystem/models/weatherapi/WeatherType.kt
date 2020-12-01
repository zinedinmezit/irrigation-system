package com.example.irrigationsystem.models.weatherapi

import com.squareup.moshi.Json

data class WeatherType(
    @Json(name = "main") val type : String,
    @Json(name = "icon") val typeIcon : String
)
