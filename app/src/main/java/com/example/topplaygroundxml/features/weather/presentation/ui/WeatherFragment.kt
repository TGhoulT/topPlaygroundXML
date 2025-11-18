package com.example.topplaygroundxml.features.weather.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.topplaygroundxml.databinding.FragmentWeatherBinding
import com.example.topplaygroundxml.features.weather.domain.model.WeatherForecast
import com.example.topplaygroundxml.features.weather.domain.model.WeatherType
import com.example.topplaygroundxml.features.weather.presentation.viewmodel.WeatherViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeatherFragment : Fragment() {

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WeatherViewModel by viewModel()
    private lateinit var adapter: WeatherAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
        setupListeners()

        // Загружаем погоду по умолчанию (Москва)
        viewModel.loadWeather()
    }

    private fun setupRecyclerView() {
        adapter = WeatherAdapter(emptyList()) { weatherData ->
            showWeatherDetails(weatherData)
        }
        binding.weatherRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.weatherRecyclerView.adapter = adapter
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.weatherState.collect { state ->
                    when (state) {
                        is WeatherViewModel.WeatherState.Loading -> {
                            binding.currentWeatherText.text = "Загрузка..."
                            binding.funnyDescriptionText.text = ""
                        }
                        is WeatherViewModel.WeatherState.Success -> {
                            displayWeather(state.forecast, viewModel.selectedCity.value)
                        }
                        is WeatherViewModel.WeatherState.Error -> {
                            binding.currentWeatherText.text = "Ошибка: ${state.message}"
                        }
                    }
                }
            }
        }
    }

    private fun setupListeners() {
        binding.fetchWeatherButton.setOnClickListener {
            val cityName = binding.cityEditText.text.toString().trim()
            if (cityName.isNotEmpty()) {
                viewModel.loadWeather(cityName)
            }
        }
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
                        "Температура: днем ${currentDay.maxTemp}°C, ночью ${currentDay.minTemp}°C"

            binding.funnyDescriptionText.text = weatherTypeDisplay.funnyDescription

            adapter.updateList(forecastList)
        }
    }

    private fun showWeatherDetails(weatherData: WeatherForecast) {
        // Показываем детали погоды (можно через Toast или Dialog)
        val domainWeatherType = WeatherType.fromApiString(weatherData.weather)
        val weatherTypeDisplay = WeatherTypeDisplay.fromDomain(domainWeatherType)

        val message = "Погода: ${weatherTypeDisplay.displayName}\n" +
                "Температура: днем ${weatherData.maxTemp}°C, ночью ${weatherData.minTemp}°C\n" +
                "Ветер: ${weatherData.windSpeed ?: "N/A"} м/с"

        android.widget.Toast.makeText(requireContext(), message, android.widget.Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}