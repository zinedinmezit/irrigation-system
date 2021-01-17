package com.example.irrigationsystem.models.sensorvalues

import com.squareup.moshi.Json

data class SensorValues(
    @Json(name = "moisture") val moistureValue : String,
    @Json(name = "dhtTemp") val dhtTempValue : String,
    @Json(name = "dhtHumm") val dhtHummValue : String,
)
