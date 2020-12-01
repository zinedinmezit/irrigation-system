package com.example.irrigationsystem.models.weatherapi

import com.squareup.moshi.Json

data class WeatherObject(
    @Json(name="list") val forecast : List<Forecast>,
    @Json(name="city") val city : City
)
