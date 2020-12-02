package com.example.irrigationsystem.models.weatherapi

import com.squareup.moshi.Json

data class WeatherMainValues(

    @Json(name="temp") val currentTemp : Double,
    @Json(name="temp_min") val minTemperature : Double,
    @Json(name ="temp_max") val maxTemperature : Double,
    @Json(name="humidity") val humidity : Int

)
