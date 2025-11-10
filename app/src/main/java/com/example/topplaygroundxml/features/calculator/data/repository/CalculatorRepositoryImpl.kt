package com.example.topplaygroundxml.features.calculator.data.repository

import com.example.topplaygroundxml.features.calculator.domain.repository.CalculatorRepository
import net.objecthunter.exp4j.ExpressionBuilder

class CalculatorRepositoryImpl : CalculatorRepository {
    override fun calculate(expression: String): Result<Double> {
        return try {
            val processedExpression = processExpression(expression)
            val result = ExpressionBuilder(processedExpression)
                .build()
                .evaluate()

            Result.success(result)
        } catch (_: Exception) {
            Result.failure(IllegalArgumentException("Неверное выражение"))
        }
    }

    private fun processExpression(expr: String): String {
        var result = expr
            .replace("×", "*")
            .replace("÷", "/")

        // Обрабатываем проценты в различных контекстах
        result = processPercentages(result)

        return result
    }

    private fun processPercentages(expr: String): String {
        if (!expr.contains('%')) return expr

        var result = expr

        // Обрабатываем случаи типа "число%число" - процент от числа
        val simplePercentage = """(\d+(?:\.\d+)?)%(\d+(?:\.\d+)?)""".toRegex()
        result = simplePercentage.replace(result) { match ->
            val (percent, number) = match.destructured
            "($percent/100)*$number"
        }

        // Обрабатываем случаи типа "число%*число" или "число%/число" и т.д.
        val percentageWithOperator = """(\d+(?:\.\d+)?)%([*/])(-?\d+(?:\.\d+)?)""".toRegex()
        result = percentageWithOperator.replace(result) { match ->
            val (percent, operator, number) = match.destructured
            when (operator) {
                "*" -> "($percent/100)*$number"
                "/" -> "($percent/100)/$number"
                else -> match.value
            }
        }

        // Обрабатываем случаи типа "число%+число" или "число%-число"
        val percentageWithAddSub = """(\d+(?:\.\d+)?)%([+-])(-?\d+(?:\.\d+)?)""".toRegex()
        result = percentageWithAddSub.replace(result) { match ->
            val (percent, operator, number) = match.destructured
            when (operator) {
                "+" -> "($percent/100)+$number"
                "-" -> "($percent/100)-$number"
                else -> match.value
            }
        }

        // Обрабатываем случаи типа "выражение+число%" - процент от предыдущего результата
        val expressionWithPercentage = """([+\-*/])(-?\d+(?:\.\d+)?)%""".toRegex()
        result = expressionWithPercentage.replace(result) { match ->
            val (op, percent) = match.destructured
            when (op) {
                "+" -> "*(1+$percent/100)"
                "-" -> "*(1-$percent/100)"
                "*" -> "*($percent/100)"
                "/" -> "/($percent/100)"
                else -> op
            }
        }

        // Обрабатываем одиночные проценты в конце
        val singlePercentage = """(\d+(?:\.\d+)?)%$""".toRegex()
        result = singlePercentage.replace(result) { match ->
            val (percent) = match.destructured
            "($percent/100)"
        }

        return result
    }
}