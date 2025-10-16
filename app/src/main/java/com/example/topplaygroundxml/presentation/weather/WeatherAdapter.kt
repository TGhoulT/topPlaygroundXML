package com.example.topplaygroundxml.presentation.weather

import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.topplaygroundxml.R
import com.example.topplaygroundxml.data.remote.DataSeries
import com.example.topplaygroundxml.databinding.ItemWeatherDataBinding
import com.example.topplaygroundxml.domain.model.WeatherType

class WeatherAdapter(
    private var weatherList: List<DataSeries>,
    private val onItemClick: (DataSeries) -> Unit = {}
) : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    inner class WeatherViewHolder(
        private val binding: ItemWeatherDataBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(weatherData: DataSeries) {
            // Преобразуем domain WeatherType в UI-представление
            val domainWeatherType = WeatherType.fromApiString(weatherData.weather)
            val weatherTypeDisplay = WeatherTypeDisplay.fromDomain(domainWeatherType)

            val (dateStr, dayOfWeek) = formatDateWithDayOfWeek(weatherData.date)

            // Добавляю пометку "Сегодня" если это текущий день
            val displayDate = if (isToday(weatherData.date)) binding.root.context.getString(R.string.today) else dateStr
            val displayDayOfWeek = if (isToday(weatherData.date)) dayOfWeek else dayOfWeek

            binding.dateText.text = displayDate
            binding.dayOfWeekText.text = displayDayOfWeek

            binding.weatherDescriptionText.text = weatherTypeDisplay.displayNameShort
            binding.dayTempText.text = binding.root.context.getString(R.string.day_temperature, weatherData.temp2m.max)
            binding.nightTempText.text = binding.root.context.getString(R.string.night_temperature, weatherData.temp2m.min)
            binding.weatherIcon.setImageResource(weatherTypeDisplay.drawableResId)

            weatherData.wind10m_max?.let { windMax ->
                binding.windText.text = binding.root.context.getString(R.string.max_wind, windMax)
            }

            // Меняем цвет карточки в зависимости от температуры
            val backgroundColor = when {
                weatherData.temp2m.max > 25 -> R.color.hot_weather
                weatherData.temp2m.max < 0 -> R.color.cold_weather
                else -> R.color.normal_weather
            }
            binding.root.setCardBackgroundColor(ContextCompat.getColor(binding.root.context, backgroundColor))

            binding.root.setOnClickListener {
                onItemClick(weatherData)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding = ItemWeatherDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(weatherList[position])
    }

    override fun getItemCount(): Int = weatherList.size

    fun updateList(newList: List<DataSeries>) {
        weatherList = newList
        notifyDataSetChanged()
    }

    private fun formatDateWithDayOfWeek(dateInt: Int): Pair<String, String> {
        val dateStr = dateInt.toString()
        if (dateStr.length != 8) return Pair(dateInt.toString(), "")

        val year = dateStr.substring(0, 4).toInt()
        val month = dateStr.substring(4, 6).toInt()
        val day = dateStr.substring(6, 8).toInt()

        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month - 1)
            set(Calendar.DAY_OF_MONTH, day)
        }

        val dayOfMonth = day
        val monthName = getMonthName(month)
        val dayOfWeek = getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK))

        return Pair("$dayOfMonth $monthName", dayOfWeek)
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

    private fun getDayOfWeek(dayOfWeek: Int): String {
        return when (dayOfWeek) {
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

    private fun isToday(dateInt: Int): Boolean {
        val dateStr = dateInt.toString()
        if (dateStr.length != 8) return false

        val today = Calendar.getInstance()
        val year = today.get(Calendar.YEAR)
        val month = today.get(Calendar.MONTH) + 1 // т.к. Calendar.MONTH начинается с 0
        val day = today.get(Calendar.DAY_OF_MONTH)

        val apiYear = dateStr.substring(0, 4).toInt()
        val apiMonth = dateStr.substring(4, 6).toInt()
        val apiDay = dateStr.substring(6, 8).toInt()

        return year == apiYear && month == apiMonth && day == apiDay
    }
}