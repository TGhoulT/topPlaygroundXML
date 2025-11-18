package com.example.topplaygroundxml.features.weather.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.topplaygroundxml.features.weather.domain.model.WeatherForecast
import com.example.topplaygroundxml.features.weather.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _weatherState = MutableStateFlow<WeatherState>(WeatherState.Loading)
    val weatherState: StateFlow<WeatherState> = _weatherState

    private val _selectedCity = MutableStateFlow("Москва")
    val selectedCity: StateFlow<String> = _selectedCity

    fun loadWeather(cityName: String = _selectedCity.value) {
        _weatherState.value = WeatherState.Loading
        _selectedCity.value = cityName

        viewModelScope.launch {
            try {
                val forecast = weatherRepository.getWeatherForecast(cityName)
                _weatherState.value = WeatherState.Success(forecast)
            } catch (e: Exception) {
                _weatherState.value = WeatherState.Error(e.message ?: "Ошибка загрузки погоды")
            }
        }
    }
    sealed class WeatherState {
        object Loading : WeatherState()
        data class Success(val forecast: List<WeatherForecast>) : WeatherState()
        data class Error(val message: String) : WeatherState()
    }
}
