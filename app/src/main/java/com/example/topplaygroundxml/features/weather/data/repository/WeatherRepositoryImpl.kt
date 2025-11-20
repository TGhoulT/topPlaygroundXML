package com.example.topplaygroundxml.features.weather.data.repository

import android.content.Context
import com.example.topplaygroundxml.R
import com.example.topplaygroundxml.features.weather.data.mapper.WeatherMapper
import com.example.topplaygroundxml.core.data.remote.RetrofitClient
import com.example.topplaygroundxml.features.weather.domain.model.WeatherForecast
import com.example.topplaygroundxml.features.weather.domain.repository.WeatherRepository

class WeatherRepositoryImpl(
    private val mapper: WeatherMapper,
    private val context: Context
) : WeatherRepository {

    private val cityCoordinates = mapOf(
        "Москва" to Pair(55.7558, 37.6173),
        "Санкт-Петербург" to Pair(59.9311, 30.3609),
        "Сочи" to Pair(43.5855, 39.7231),
        "Челябинск" to Pair(55.1644, 61.4368),
        "Новосибирск" to Pair(55.0084, 82.9357),
        "Екатеринбург" to Pair(56.8389, 60.6057),
        "Владивосток" to Pair(43.1155, 131.8855)
    )

    override suspend fun getWeatherForecast(cityName: String): List<WeatherForecast> {
        try {
            val coordinates = cityCoordinates[cityName]
                ?: throw IllegalArgumentException(context.getString(R.string.city_not_found_error, cityName))

            val weatherResponse = RetrofitClient.weatherApi.getWeatherForecast(
                longitude = coordinates.second,
                latitude = coordinates.first
            )

            // Проверяем, что ответ не пустой
            if (weatherResponse.dataseries.isEmpty()) {
                throw IllegalStateException(context.getString(R.string.no_weather_data))
            }

            return mapper.toWeatherForecastList(weatherResponse)
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.weather_error, e.message ?: R.string.weather_unknown_error))
        }
    }
}