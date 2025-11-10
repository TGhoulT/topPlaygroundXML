package com.example.topplaygroundxml.features.weather.data.mapper

import android.content.Context
import com.example.topplaygroundxml.R
import com.example.topplaygroundxml.features.weather.data.remote.WeatherResponse
import com.example.topplaygroundxml.features.weather.domain.model.WeatherForecast
import java.util.Calendar

class WeatherMapper(
    private val context: Context
) {

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
            Calendar.MONDAY -> context.getString(R.string.monday)
            Calendar.TUESDAY -> context.getString(R.string.tuesday)
            Calendar.WEDNESDAY -> context.getString(R.string.wednesday)
            Calendar.THURSDAY -> context.getString(R.string.thursday)
            Calendar.FRIDAY -> context.getString(R.string.friday)
            Calendar.SATURDAY -> context.getString(R.string.saturday)
            Calendar.SUNDAY -> context.getString(R.string.sunday)
            else -> ""
        }
    }

    private fun getMonthName(month: Int): String {
        return when (month) {
            1 -> context.getString(R.string.january)
            2 -> context.getString(R.string.february)
            3 -> context.getString(R.string.march)
            4 -> context.getString(R.string.april)
            5 -> context.getString(R.string.may)
            6 -> context.getString(R.string.june)
            7 -> context.getString(R.string.july)
            8 -> context.getString(R.string.august)
            9 -> context.getString(R.string.september)
            10 -> context.getString(R.string.october)
            11 -> context.getString(R.string.november)
            12 -> context.getString(R.string.december)
            else -> ""
        }
    }

    private fun isToday(dateInt: Int): Boolean {
        val dateStr = dateInt.toString()
        if (dateStr.length != 8) return false

        val today = Calendar.getInstance()
        val year = today.get(Calendar.YEAR)
        val month = today.get(Calendar.MONTH) + 1
        val day = today.get(Calendar.DAY_OF_MONTH)

        val apiYear = dateStr.substring(0, 4).toInt()
        val apiMonth = dateStr.substring(4, 6).toInt()
        val apiDay = dateStr.substring(6, 8).toInt()

        return year == apiYear && month == apiMonth && day == apiDay
    }
}