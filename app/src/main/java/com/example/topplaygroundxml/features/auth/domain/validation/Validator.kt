package com.example.topplaygroundxml.features.auth.domain.validation

import android.content.Context
import com.example.topplaygroundxml.R

class Validator(private val context: Context) {

    fun validateEmail(email: String): ValidationResult {
        return when {
            email.isBlank() -> ValidationResult.Error(context.getString(R.string.error_email_empty))
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                ValidationResult.Error(context.getString(R.string.error_invalid_email))
            else -> ValidationResult.Success
        }
    }

    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isBlank() -> ValidationResult.Error(context.getString(R.string.error_password_empty))
            password.length < 6 -> ValidationResult.Error(context.getString(R.string.error_password_too_short))
            else -> ValidationResult.Success
        }
    }

    fun validateConfirmPassword(password: String, confirmPassword: String): ValidationResult {
        return when {
            confirmPassword.isBlank() -> ValidationResult.Error(context.getString(R.string.error_confirm_password_empty))
            password != confirmPassword -> ValidationResult.Error(context.getString(R.string.error_passwords_not_match))
            else -> ValidationResult.Success
        }
    }

    fun getInvalidCredentialsError(): String {
        return context.getString(R.string.error_invalid_credentials)
    }

    fun getUserAlreadyExistsError(): String {
        return context.getString(R.string.error_user_already_exists)
    }

    fun getLoginError(exceptionMessage: String?): String {
        return context.getString(R.string.error_login_generic, exceptionMessage ?: "")
    }

    fun getRegistrationError(exceptionMessage: String?): String {
        return context.getString(R.string.error_registration_generic, exceptionMessage ?: "")
    }
}