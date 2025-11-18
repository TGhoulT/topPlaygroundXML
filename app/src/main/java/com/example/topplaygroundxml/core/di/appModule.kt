package com.example.topplaygroundxml.core.di

import com.example.topplaygroundxml.core.data.local.AppDatabase
import com.example.topplaygroundxml.core.data.remote.RetrofitClient
import com.example.topplaygroundxml.features.auth.data.repository.AuthRepositoryImpl
import com.example.topplaygroundxml.features.auth.domain.repository.AuthRepository
import com.example.topplaygroundxml.features.auth.presentation.viewmodel.AuthViewModel
import com.example.topplaygroundxml.features.calculator.data.repository.CalculatorRepositoryImpl
import com.example.topplaygroundxml.features.calculator.domain.repository.CalculatorRepository
import com.example.topplaygroundxml.features.calculator.domain.usecase.CalculateUseCase
import com.example.topplaygroundxml.features.calculator.presentation.viewmodel.CalculatorViewModel
import com.example.topplaygroundxml.features.carlist.presentation.viewmodel.CarListViewModel
import com.example.topplaygroundxml.features.weather.data.mapper.WeatherMapper
import com.example.topplaygroundxml.features.weather.data.repository.WeatherRepositoryImpl
import com.example.topplaygroundxml.features.weather.domain.repository.WeatherRepository
import com.example.topplaygroundxml.features.weather.presentation.viewmodel.WeatherViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // БД
    single { AppDatabase.getInstance(get()) }
    single { get<AppDatabase>().userDao() }

    // Аутентификация
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    viewModel { AuthViewModel(get()) }

    // Список авто
    viewModel { CarListViewModel() }

    // Погода
    single { RetrofitClient.weatherApi }
    single<WeatherRepository> { WeatherRepositoryImpl(get(), get()) }
    single { WeatherMapper(get()) }
    viewModel { WeatherViewModel(get()) }

    // Калькулятор
    single<CalculatorRepository> { CalculatorRepositoryImpl() }
    single { CalculateUseCase(get()) }
    viewModel { CalculatorViewModel(get()) }

}