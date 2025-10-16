package com.example.topplaygroundxml.domain.model

enum class WeatherType {
    CLEAR_DAY, PARTLY_CLOUDY, CLOUDY, FOG, LIGHT_RAIN,
    RAIN, SNOW, THUNDERSTORM, WINDY, UNKNOWN;

    companion object {
        fun fromApiString(apiWeather: String): WeatherType {
            return when (apiWeather.lowercase()) {
                "clear" -> CLEAR_DAY
                "pcloudy" -> PARTLY_CLOUDY
                "mcloudy", "cloudy" -> CLOUDY
                "fog", "humid" -> FOG
                "lightrain", "oshower", "ishower" -> LIGHT_RAIN
                "rain" -> RAIN
                "snow", "lightsnow" -> SNOW
                "ts", "tsrain" -> THUNDERSTORM
                "windy" -> WINDY
                else -> UNKNOWN
            }
        }
    }
}