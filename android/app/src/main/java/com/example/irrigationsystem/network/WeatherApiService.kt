package com.example.irrigationsystem.network

import com.example.irrigationsystem.models.weatherapi.WeatherObject
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val API_BASE_URL = "http://api.openweathermap.org/data/2.5/"
private const val API_KEY = "18d2446da5446dabbacc445d82fa25bd"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(API_BASE_URL)
    .build()

interface WeatherApiService{
    @GET("forecast")
    suspend fun getWeatherForCity(@Query("q") cityName : String,
                                  @Query("units") metric : String = "metric",
                                  @Query("cnt") count : String = "4",
                                  @Query("appid") apiKey : String = API_KEY) : WeatherObject
}

object WeatherApi{
    val retrofitService : WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}