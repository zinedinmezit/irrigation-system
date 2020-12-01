package com.example.irrigationsystem.models.weatherapi

import com.squareup.moshi.Json

data class City(
    @Json(name = "name") val cityName : String,
    @Json(name = "country") val cityCountry : String,
    @Json(name = "sunrise") val citySunrise : Long,
    @Json(name = "sunset") val citySunset : Long
)
