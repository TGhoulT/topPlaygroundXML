package com.example.topplaygroundxml.features.calculator.presentation.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.topplaygroundxml.features.calculator.domain.usecase.CalculateUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CalculatorViewModel(
    private val calculateUseCase: CalculateUseCase
) : ViewModel() {

    private val _expression = MutableStateFlow("0")
    val expression: StateFlow<String> = _expression.asStateFlow()

    private val _result = MutableStateFlow("0")
    val result: StateFlow<String> = _result.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun onNumberClick(number: String) {
        val current = _expression.value

        _expression.value = when {
            current == "0" && number == "00" -> "0"
            current == "0" -> number
            current == "0." -> "0.$number"
            else -> current + number
        }
        _error.value = null
    }

    fun onOperationClick(operation: String) {
        val current = _expression.value

        if (current.isEmpty()) {
            if (operation == "-") {
                _expression.value = operation
            }
            return
        }

        val lastChar = current.last()

        when {
            // Если последний символ - цифра, точка или процент, то добавляем оператор
            lastChar.isDigit() || lastChar == '.' || lastChar == '%' -> {
                _expression.value = current + operation
            }
            // Если последний символ - оператор (+, ×, ÷) и нажимаем минус, то добавляем минус (унарный)
            lastChar in "+×÷" && operation == "-" -> {
                _expression.value = current + operation
            }
            // Если последний символ - оператор (+, ×, ÷) и нажимаем другой оператор (не минус), то заменяем
            lastChar in "+×÷" && operation != "-" -> {
                _expression.value = current.dropLast(1) + operation
            }
            // Если последний символ - минус
            lastChar == '-' -> {
                // Проверяем, является ли минус унарным (перед ним есть оператор)
                val isUnary = current.length > 1 && current[current.length - 2] in "+×÷"
                if (operation != "-") {
                    if (isUnary) {
                        // Унарный минус: заменяем весь блок (оператор + минус) на новый оператор
                        _expression.value = current.dropLast(2) + operation
                    } else {
                        // Бинарный минус: заменяем на новый оператор
                        _expression.value = current.dropLast(1) + operation
                    }
                }
                // Если нажимаем минус, то ничего не делаем (запрещаем двойной минус)
            }
        }
        _error.value = null
    }

    fun onPercentageClick() {
        val current = _expression.value
        if (current.isNotEmpty() && (current.last().isDigit() || current.last() == '.')) {
            // Просто добавляем знак процента, не вычисляем сразу
            _expression.value = "$current%"
        }
        _error.value = null
    }

    fun onDecimalClick() {
        val current = _expression.value
        if (current.isNotEmpty()) {
            // Находим последнее число в выражении
            val lastNumberPart = current.split(*"+-×÷%".toCharArray()).last()
            if (!lastNumberPart.contains('.')) {
                _expression.value = if (current.last().isDigit()) {
                    "$current."
                } else {
                    current + "0."
                }
            }
        }
        _error.value = null
    }

    fun onClearClick() {
        _expression.value = "0"
        _result.value = "0"
        _error.value = null
    }

    fun onDeleteClick() {
        val current = _expression.value
        if (current.isNotEmpty() && current != "0") {
            val newExpression = current.dropLast(1)
            _expression.value = newExpression.ifEmpty { "0" }
            _error.value = null
        }
    }

    fun onEqualsClick() {
        val expr = _expression.value
        if (expr.isBlank() || expr == "0") return

        viewModelScope.launch {
            _error.value = null
            val calculationResult = calculateUseCase(expr)

            calculationResult.fold(
                onSuccess = { calcResult ->
                    _result.value = formatResult(calcResult)
                    // Обновляем выражение результатом для продолжения вычислений
                    _expression.value = formatResult(calcResult)
                },
                onFailure = { e ->
                    _error.value = "Ошибка"
                    _result.value = "Ошибка"
                }
            )
        }
    }

    @SuppressLint("DefaultLocale")
    private fun formatResult(result: Double): String {
        return if (result % 1 == 0.0) {
            result.toInt().toString()
        } else {
            // Форматируем до 10 знаков и убираем лишние нули
            String.format("%.10f", result)
                .trimEnd('0')
                .trimEnd('.')
                .let { it.ifEmpty { "0" } }
        }
    }
}