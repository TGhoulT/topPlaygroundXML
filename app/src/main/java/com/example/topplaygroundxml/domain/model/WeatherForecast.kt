package com.example.topplaygroundxml.domain.model

data class WeatherForecast(
    val date: Int,
    val formattedDate: String,
    val dayOfWeek: String,
    val weather: String,
    val maxTemp: Int,
    val minTemp: Int,
    val windSpeed: Int?,
    val isToday: Boolean = false
)