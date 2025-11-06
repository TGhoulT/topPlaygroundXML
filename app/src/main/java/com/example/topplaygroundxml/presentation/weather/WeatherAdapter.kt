package com.example.topplaygroundxml.presentation.weather

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.topplaygroundxml.R
import com.example.topplaygroundxml.databinding.ItemWeatherDataBinding
import com.example.topplaygroundxml.domain.model.WeatherType

class WeatherAdapter(
    private var weatherList: List<com.example.topplaygroundxml.domain.model.WeatherForecast>,
    private val onItemClick: (com.example.topplaygroundxml.domain.model.WeatherForecast) -> Unit = {}
) : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    inner class WeatherViewHolder(
        private val binding: ItemWeatherDataBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(weatherForecast: com.example.topplaygroundxml.domain.model.WeatherForecast) {
            val domainWeatherType = WeatherType.fromApiString(weatherForecast.weather)
            val weatherTypeDisplay = WeatherTypeDisplay.fromDomain(domainWeatherType)

            // Используем уже отформатированные данные из domain модели
            val displayDate = if (weatherForecast.isToday)
                binding.root.context.getString(R.string.today)
            else
                weatherForecast.formattedDate

            val displayDayOfWeek = weatherForecast.dayOfWeek

            binding.dateText.text = displayDate
            binding.dayOfWeekText.text = displayDayOfWeek
            binding.weatherDescriptionText.text = weatherTypeDisplay.displayNameShort
            binding.dayTempText.text = binding.root.context.getString(R.string.day_temperature, weatherForecast.maxTemp)
            binding.nightTempText.text = binding.root.context.getString(R.string.night_temperature, weatherForecast.minTemp)
            binding.weatherIcon.setImageResource(weatherTypeDisplay.drawableResId)

            weatherForecast.windSpeed?.let { windMax ->
                binding.windText.text = binding.root.context.getString(R.string.max_wind, windMax)
            }

            // Меняем цвет карточки в зависимости от температуры
            val backgroundColor = when {
                weatherForecast.maxTemp > 25 -> R.color.hot_weather
                weatherForecast.maxTemp < 0 -> R.color.cold_weather
                else -> R.color.normal_weather
            }
            binding.root.setCardBackgroundColor(ContextCompat.getColor(binding.root.context, backgroundColor))

            binding.root.setOnClickListener {
                onItemClick(weatherForecast)
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

    fun updateList(newList: List<com.example.topplaygroundxml.domain.model.WeatherForecast>) {
        weatherList = newList
        notifyDataSetChanged()
    }
}