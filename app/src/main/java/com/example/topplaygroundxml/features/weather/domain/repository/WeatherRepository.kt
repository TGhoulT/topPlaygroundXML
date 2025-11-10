package com.example.topplaygroundxml.features.weather.domain.repository

import com.example.topplaygroundxml.features.weather.domain.model.WeatherForecast

interface WeatherRepository {
    suspend fun getWeatherForecast(cityName: String): List<WeatherForecast>
}