package com.example.topplaygroundxml.presentation.weather

import com.example.topplaygroundxml.R
import com.example.topplaygroundxml.domain.model.WeatherType

// Класс отвечает за преобразование domain WeatherType в UI-представление
data class WeatherTypeDisplay(
    val displayName: String,
    val displayNameShort: String,
    val emoji: String,
    val drawableResId: Int,
    val backgroundResId: Int,
    val funnyDescription: String
) {
    companion object {
        fun fromDomain(weatherType: WeatherType): WeatherTypeDisplay {
            return when (weatherType) {
                WeatherType.CLEAR_DAY -> WeatherTypeDisplay(
                    "Ясно",
                    "Ясно",
                    "☀️",
                    R.drawable.weather_clear,
                    R.drawable.bg_sunny,
                    "Ясно. Что ему ясно?"
                )
                WeatherType.PARTLY_CLOUDY -> WeatherTypeDisplay(
                    "Переменная облачность",
                    "Перем. облачн.",
                    "⛅",
                    R.drawable.weather_partly_cloudy,
                    R.drawable.bg_partly_cloudy,
                    "Ну пойдёт"
                )
                WeatherType.CLOUDY -> WeatherTypeDisplay(
                    "Облачно",
                    "Облачно",
                    "☁️",
                    R.drawable.weather_cloudy,
                    R.drawable.bg_cloudy,
                    "Преимущественно?"
                )
                WeatherType.FOG -> WeatherTypeDisplay(
                    "Туман",
                    "Туман",
                    "🌫️",
                    R.drawable.weather_fog,
                    R.drawable.bg_fog,
                    "Ёжик..."
                )
                WeatherType.LIGHT_RAIN -> WeatherTypeDisplay(
                    "Небольшой дождь",
                    "Мал. дождь",
                    "🌦️",
                    R.drawable.weather_light_rain,
                    R.drawable.bg_light_rain,
                    ""
                )
                WeatherType.RAIN -> WeatherTypeDisplay(
                    "Дождь",
                    "Дождь",
                    "🌧️",
                    R.drawable.weather_rain,
                    R.drawable.bg_rain,
                    "Можно нюхать озон сколько угодно"
                )
                WeatherType.SNOW -> WeatherTypeDisplay(
                    "Снег",
                    "Снег",
                    "❄️",
                    R.drawable.weather_snow,
                    R.drawable.bg_snow,
                    "Снеговик. Улица. Лепить."
                )
                WeatherType.THUNDERSTORM -> WeatherTypeDisplay(
                    "Гроза",
                    "Гроза",
                    "⛈️",
                    R.drawable.weather_thunderstorm,
                    R.drawable.bg_thunderstorm,
                    "Бабах."
                )
                WeatherType.WINDY -> WeatherTypeDisplay(
                    "Ветренно",
                    "Ветренно",
                    "💨",
                    R.drawable.weather_windy,
                    R.drawable.bg_windy,
                    "Желательно надеть шарф и хорошую ветровку."
                )
                WeatherType.UNKNOWN -> WeatherTypeDisplay(
                    "Неизвестно",
                    "Неизвестно",
                    "🌈",
                    R.drawable.weather_unknown,
                    R.drawable.bg_default,
                    "Погода загадочна..."
                )
            }
        }
    }
}