package com.example.irrigationsystem.network

import com.example.irrigationsystem.models.sensorvalues.SensorValues
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object MoshiProvider{

   private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    val jsonAdapter: JsonAdapter<SensorValues> = moshi.adapter(SensorValues::class.java)
}