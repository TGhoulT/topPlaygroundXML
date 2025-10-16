package com.example.topplaygroundxml.data.remote

data class WeatherResponse(
    val product: String,
    val init: String,
    val dataseries: List<DataSeries>
)

data class DataSeries(
    val date: Int,
    val weather: String,
    val temp2m: Temp2m,
    val wind10m_max: Int? = null
)

data class Temp2m(
    val max: Int,
    val min: Int
)