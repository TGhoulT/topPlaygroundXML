package com.example.topplaygroundxml.data.mapper

import com.example.topplaygroundxml.data.remote.WeatherResponse
import com.example.topplaygroundxml.domain.model.WeatherForecast
import java.util.Calendar

class WeatherMapper {

    fun toWeatherForecastList(weatherResponse: WeatherResponse): List<WeatherForecast> {
        return weatherResponse.dataseries.map { dataSeries ->
            WeatherForecast(
                date = dataSeries.date,
                formattedDate = formatDate(dataSeries.date),
                dayOfWeek = getDayOfWeek(dataSeries.date),
                weather = dataSeries.weather,
                maxTemp = dataSeries.temp2m.max,
                minTemp = dataSeries.temp2m.min,
                windSpeed = dataSeries.wind10m_max,
                isToday = isToday(dataSeries.date)
            )
        }
    }

    private fun formatDate(dateInt: Int): String {
        val dateStr = dateInt.toString()
        if (dateStr.length != 8) return dateInt.toString()

        val year = dateStr.substring(0, 4).toInt()
        val month = dateStr.substring(4, 6).toInt()
        val day = dateStr.substring(6, 8).toInt()

        return "$day ${getMonthName(month)}"
    }

    private fun getDayOfWeek(dateInt: Int): String {
        val dateStr = dateInt.toString()
        if (dateStr.length != 8) return ""

        val year = dateStr.substring(0, 4).toInt()
        val month = dateStr.substring(4, 6).toInt() - 1
        val day = dateStr.substring(6, 8).toInt()

        val calendar = Calendar.getInstance().apply {
            set(year, month, day)
        }

        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> "Понедельник"
            Calendar.TUESDAY -> "Вторник"
            Calendar.WEDNESDAY -> "Среда"
            Calendar.THURSDAY -> "Четверг"
            Calendar.FRIDAY -> "Пятница"
            Calendar.SATURDAY -> "Суббота"
            Calendar.SUNDAY -> "Воскресенье"
            else -> ""
        }
    }

    private fun getMonthName(month: Int): String {
        return when (month) {
            1 -> "января"
            2 -> "февраля"
            3 -> "марта"
            4 -> "апреля"
            5 -> "мая"
            6 -> "июня"
            7 -> "июля"
            8 -> "августа"
            9 -> "сентября"
            10 -> "октября"
            11 -> "ноября"
            12 -> "декабря"
            else -> ""
        }
    }

    private fun isToday(dateInt: Int): Boolean {
        val dateStr = dateInt.toString()
        if (dateStr.length != 8) return false

        val today = android.icu.util.Calendar.getInstance()
        val year = today.get(android.icu.util.Calendar.YEAR)
        val month = today.get(android.icu.util.Calendar.MONTH) + 1 // т.к. Calendar.MONTH начинается с 0
        val day = today.get(android.icu.util.Calendar.DAY_OF_MONTH)

        val apiYear = dateStr.substring(0, 4).toInt()
        val apiMonth = dateStr.substring(4, 6).toInt()
        val apiDay = dateStr.substring(6, 8).toInt()

        return year == apiYear && month == apiMonth && day == apiDay
    }
}