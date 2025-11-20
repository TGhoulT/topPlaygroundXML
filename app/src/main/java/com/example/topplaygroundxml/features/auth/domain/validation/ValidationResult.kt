package com.example.topplaygroundxml.features.auth.domain.validation

sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()
}