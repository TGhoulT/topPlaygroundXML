package com.example.topplaygroundxml.core.di

import com.example.topplaygroundxml.core.data.remote.RetrofitClient
import com.example.topplaygroundxml.features.calculator.data.repository.CalculatorRepositoryImpl
import com.example.topplaygroundxml.features.calculator.domain.repository.CalculatorRepository
import com.example.topplaygroundxml.features.calculator.domain.usecase.CalculateUseCase
import com.example.topplaygroundxml.features.calculator.presentation.viewmodel.CalculatorViewModel
import com.example.topplaygroundxml.features.weather.data.mapper.WeatherMapper
import com.example.topplaygroundxml.features.weather.data.repository.WeatherRepositoryImpl
import com.example.topplaygroundxml.features.weather.domain.repository.WeatherRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Погода
    single { RetrofitClient.weatherApi }
    single<WeatherRepository> { WeatherRepositoryImpl(get(), get()) }
    single { WeatherMapper(get()) }

    // Калькулятор
    single<CalculatorRepository> { CalculatorRepositoryImpl() }
    single { CalculateUseCase(get()) }
    viewModel { CalculatorViewModel(get()) }
}