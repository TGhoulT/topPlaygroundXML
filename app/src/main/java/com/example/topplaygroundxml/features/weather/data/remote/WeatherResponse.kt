package com.example.topplaygroundxml.features.weather.data.remote

data class WeatherResponse(
    val product: String? = null,
    val init: String? = null,
    val dataseries: List<DataSeries> = emptyList()
)

data class DataSeries(
    val date: Int = 0,
    val weather: String = "unknown",
    val temp2m: Temp2m = Temp2m(),
    val wind10m_max: Int? = null
)

data class Temp2m(
    val max: Int? = null,
    val min: Int? = null
)