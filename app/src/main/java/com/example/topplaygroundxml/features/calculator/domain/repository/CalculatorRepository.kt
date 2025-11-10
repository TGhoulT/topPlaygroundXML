package com.example.topplaygroundxml.features.calculator.domain.repository

interface CalculatorRepository {
    fun calculate(expression: String): Result<Double>
}