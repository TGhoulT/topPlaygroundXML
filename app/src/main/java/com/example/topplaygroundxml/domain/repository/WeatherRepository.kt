package com.example.topplaygroundxml.domain.repository

import com.example.topplaygroundxml.domain.model.WeatherForecast

interface WeatherRepository {
    suspend fun getWeatherForecast(cityName: String): List<WeatherForecast>
}