package com.example.topplaygroundxml.features.weather.presentation.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.topplaygroundxml.R
import com.example.topplaygroundxml.databinding.ActivityWeatherBinding
import com.example.topplaygroundxml.features.weather.domain.model.WeatherForecast
import com.example.topplaygroundxml.features.weather.domain.model.WeatherType
import com.example.topplaygroundxml.features.weather.domain.repository.WeatherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

class WeatherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherBinding
    private lateinit var adapter: WeatherAdapter
    private val TAG = "WeatherApp"

    private val repository: WeatherRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.weatherRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = WeatherAdapter(emptyList()) { weatherData ->
            showWeatherDetails(weatherData)
        }
        binding.weatherRecyclerView.adapter = adapter

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.fetchWeatherButton.setOnClickListener {
            val cityName = binding.cityEditText.text.toString().trim()
            fetchWeatherForCity(cityName)
        }

        //загружаем погоду для Москвы по умолчанию
        fetchWeatherForCity(getString(R.string.moscow_default))
    }

    private fun fetchWeatherForCity(cityName: String) {
        binding.currentWeatherText.text = getString(R.string.loading_weather_for, cityName)
        binding.funnyDescriptionText.text = ""

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val forecastList = repository.getWeatherForecast(cityName)

                withContext(Dispatchers.Main) {
                    displayWeather(forecastList, cityName)
                    logWeatherData(forecastList, cityName)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.currentWeatherText.text = getString(R.string.weather_error, e.message)
                }
                Log.e(TAG, "Ошибка при получении погоды: ${e.message}", e)
            }
        }
    }

    private fun logWeatherData(forecastList: List<WeatherForecast>, cityName: String) {
        Log.d(TAG, "=== ПРОГНОЗ ПОГОДЫ НА НЕДЕЛЮ ===")
        Log.d(TAG, "Город: $cityName")
        Log.d(TAG, "Количество дней: ${forecastList.size}")

        forecastList.forEachIndexed { index, day ->
            val domainWeatherType = WeatherType.fromApiString(day.weather)
            val weatherTypeDisplay = WeatherTypeDisplay.fromDomain(domainWeatherType)

            Log.d(TAG, "-------------------")
            Log.d(TAG, "День ${index + 1}:")
            Log.d(TAG, "• Дата: ${day.date} (${day.formattedDate})")
            Log.d(TAG, "• День недели: ${day.dayOfWeek}")
            Log.d(TAG, "• Погода: ${weatherTypeDisplay.displayName} ${weatherTypeDisplay.emoji}")
            Log.d(TAG, "• Температура: макс ${day.maxTemp}°C, мин ${day.minTemp}°C")
            Log.d(TAG, "• Шутка: ${weatherTypeDisplay.funnyDescription}")
            day.windSpeed?.let { windMax ->
                Log.d(TAG, "• Максимальный ветер: $windMax м/с")
            }
        }
        Log.d(TAG, "=== КОНЕЦ ПРОГНОЗА ===")
    }

    private fun displayWeather(forecastList: List<WeatherForecast>, cityName: String) {
        val currentDay = forecastList.firstOrNull()

        if (currentDay != null) {
            val domainWeatherType = WeatherType.fromApiString(currentDay.weather)
            val weatherTypeDisplay = WeatherTypeDisplay.fromDomain(domainWeatherType)

            binding.weatherIcon.setImageResource(weatherTypeDisplay.drawableResId)
            binding.root.setBackgroundResource(weatherTypeDisplay.backgroundResId)

            binding.currentWeatherText.text =
                "$cityName: ${weatherTypeDisplay.displayName} ${weatherTypeDisplay.emoji}\n" +
                        getString(R.string.temperature_format, currentDay.maxTemp, currentDay.minTemp)

            binding.funnyDescriptionText.text = weatherTypeDisplay.funnyDescription

            adapter.updateList(forecastList)
        }
    }

    private fun showWeatherDetails(weatherData: WeatherForecast) {
        val domainWeatherType = WeatherType.fromApiString(weatherData.weather)
        val weatherTypeDisplay = WeatherTypeDisplay.fromDomain(domainWeatherType)

        val message = getString(
            R.string.weather_details_format,
            weatherTypeDisplay.displayName,
            weatherData.maxTemp,
            weatherData.minTemp,
            weatherData.windSpeed ?: getString(R.string.na)
        )

        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}