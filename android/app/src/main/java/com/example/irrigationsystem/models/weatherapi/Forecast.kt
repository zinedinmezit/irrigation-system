package com.example.irrigationsystem.models.weatherapi

import com.squareup.moshi.Json

data class Forecast(
    @Json(name="main") val weatherMainValues : WeatherMainValues,
    @Json(name="weather") val weatherType : List<WeatherType>,
    @Json(name="dt_txt") val timeStampString : String
)
