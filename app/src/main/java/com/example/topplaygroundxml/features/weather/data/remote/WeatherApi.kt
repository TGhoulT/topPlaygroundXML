package com.example.topplaygroundxml.features.weather.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("bin/api.pl")
    suspend fun getWeatherForecast(
        @Query("lon") longitude: Double,
        @Query("lat") latitude: Double,
        @Query("product") product: String = "civillight",
        @Query("output") output: String = "json"
    ): WeatherResponse
}