package com.example.topplaygroundxml.presentation.weather

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.topplaygroundxml.R
import com.example.topplaygroundxml.data.remote.DataSeries
import com.example.topplaygroundxml.data.remote.RetrofitClient
import com.example.topplaygroundxml.data.remote.WeatherResponse
import com.example.topplaygroundxml.databinding.ActivityWeatherBinding
import com.example.topplaygroundxml.domain.model.WeatherType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeatherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherBinding
    private lateinit var adapter: WeatherAdapter
    private val TAG = "WeatherApp"

    //координаты городов (расширить потом)
    private val cityCoordinates = mapOf(
        "Москва" to Pair(55.7558, 37.6173),
        "Санкт-Петербург" to Pair(59.9311, 30.3609),
        "Сочи" to Pair(43.5855, 39.7231),
        "Челябинск" to Pair(55.1644, 61.4368),
        "Новосибирск" to Pair(55.0084, 82.9357),
        "Екатеринбург" to Pair(56.8389, 60.6057),
        "Владивосток" to Pair(43.1155, 131.8855)
    )

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
        val coordinates = cityCoordinates[cityName] ?: run {
            binding.currentWeatherText.text = getString(R.string.city_not_found)
            return
        }

        val (latitude, longitude) = coordinates

        binding.currentWeatherText.text = getString(R.string.loading_weather_for, cityName)
        binding.funnyDescriptionText.text = ""

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val weatherData = RetrofitClient.weatherApi.getWeatherForecast(
                    longitude = longitude,
                    latitude = latitude
                )

                // ДОБАВИМ ЛОГИРОВАНИЕ ДЛЯ ДИАГНОСТИКИ
                Log.d(TAG, "=== RAW API RESPONSE ===")
                Log.d(TAG, "Product: ${weatherData.product}")
                Log.d(TAG, "Init: ${weatherData.init}")
                weatherData.dataseries.forEachIndexed { index, data ->
                    Log.d(TAG, "Day $index: weather='${data.weather}', temp2m=${data.temp2m}")
                }

                // выводим в лог
                logWeatherData(weatherData, cityName)

                withContext(Dispatchers.Main) {
                    displayWeather(weatherData, cityName)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.currentWeatherText.text = getString(R.string.weather_error, e.message)
                }
                Log.e(TAG, "Ошибка при получении погоды: ${e.message}", e)
            }
        }
    }

    private fun logWeatherData(weatherResponse: WeatherResponse, cityName: String) {
        Log.d(TAG, "=== ПРОГНОЗ ПОГОДЫ НА НЕДЕЛЮ ===")
        Log.d(TAG, "Город: $cityName")
        Log.d(TAG, "Продукт: ${weatherResponse.product}")
        Log.d(TAG, "Время инициализации: ${weatherResponse.init}")
        Log.d(TAG, "Количество дней: ${weatherResponse.dataseries.size}")

        weatherResponse.dataseries.forEachIndexed { index, day ->
            // Преобразуем domain WeatherType в UI-представление для логирования
            val domainWeatherType = WeatherType.fromApiString(day.weather)
            val weatherTypeDisplay = WeatherTypeDisplay.fromDomain(domainWeatherType)

            Log.d(TAG, "-------------------")
            Log.d(TAG, "День ${index + 1}:")
            Log.d(TAG, "• Дата: ${day.date}")
            Log.d(TAG, "• Погода: ${weatherTypeDisplay.displayName} ${weatherTypeDisplay.emoji}")
            Log.d(TAG, "• Температура: макс ${day.temp2m.max}°C, мин ${day.temp2m.min}°C")
            Log.d(TAG, "• Шутка: ${weatherTypeDisplay.funnyDescription}")
            day.wind10m_max?.let { windMax ->
                Log.d(TAG, "• Максимальный ветер: $windMax м/с")
            }
        }
        Log.d(TAG, "=== КОНЕЦ ПРОГНОЗА ===")
    }

    private fun displayWeather(weatherResponse: WeatherResponse, cityName: String) {
        val currentDay = weatherResponse.dataseries.firstOrNull()

        if (currentDay != null) {
            // преобразуем domain WeatherType в UI-представление
            val domainWeatherType = WeatherType.fromApiString(currentDay.weather)
            val weatherTypeDisplay = WeatherTypeDisplay.fromDomain(domainWeatherType)

            binding.weatherIcon.setImageResource(weatherTypeDisplay.drawableResId)
            binding.root.setBackgroundResource(weatherTypeDisplay.backgroundResId)

            binding.currentWeatherText.text =
                "$cityName: ${weatherTypeDisplay.displayName} ${weatherTypeDisplay.emoji}\n" +
                        getString(R.string.temperature_format, currentDay.temp2m.max, currentDay.temp2m.min)

            binding.funnyDescriptionText.text = weatherTypeDisplay.funnyDescription

            adapter.updateList(weatherResponse.dataseries)
        }
    }

    private fun showWeatherDetails(weatherData: DataSeries) {
        // преобразуем для деталей
        val domainWeatherType = WeatherType.fromApiString(weatherData.weather)
        val weatherTypeDisplay = WeatherTypeDisplay.fromDomain(domainWeatherType)

        val message = getString(
            R.string.weather_details_format,
            weatherTypeDisplay.displayName,
            weatherData.temp2m.max,
            weatherData.temp2m.min,
            weatherData.wind10m_max ?: getString(R.string.na)
        )

        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}