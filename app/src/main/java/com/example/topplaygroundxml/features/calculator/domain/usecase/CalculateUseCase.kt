package com.example.topplaygroundxml.features.calculator.domain.usecase

import com.example.topplaygroundxml.features.calculator.domain.repository.CalculatorRepository

class CalculateUseCase(
    private val repository: CalculatorRepository
) {
    operator fun invoke(expression: String): Result<Double> {
        return repository.calculate(expression)
    }
}