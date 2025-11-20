package com.example.topplaygroundxml.features.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.topplaygroundxml.features.auth.domain.repository.AuthRepository
import com.example.topplaygroundxml.features.auth.domain.validation.Validator
import com.example.topplaygroundxml.features.auth.domain.validation.ValidationResult
import com.example.topplaygroundxml.features.auth.presentation.viewmodel.state.LoginState
import com.example.topplaygroundxml.features.auth.presentation.viewmodel.state.RegisterState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val validator: Validator
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    fun login(email: String, password: String) {
        // Валидация email
        when (val emailValidation = validator.validateEmail(email)) {
            is ValidationResult.Error -> {
                _loginState.value = LoginState.Error(emailValidation.message)
                return
            }
            else -> {}
        }

        // Валидация пароля
        when (val passwordValidation = validator.validatePassword(password)) {
            is ValidationResult.Error -> {
                _loginState.value = LoginState.Error(passwordValidation.message)
                return
            }
            else -> {}
        }

        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            try {
                val success = authRepository.login(email, password)
                _loginState.value = if (success) {
                    LoginState.Success
                } else {
                    LoginState.Error(validator.getInvalidCredentialsError())
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(validator.getLoginError(e.message))
            }
        }
    }

    fun register(email: String, password: String, confirmPassword: String) {
        // Валидация email
        when (val emailValidation = validator.validateEmail(email)) {
            is ValidationResult.Error -> {
                _registerState.value = RegisterState.Error(emailValidation.message)
                return
            }
            else -> {}
        }

        // Валидация пароля
        when (val passwordValidation = validator.validatePassword(password)) {
            is ValidationResult.Error -> {
                _registerState.value = RegisterState.Error(passwordValidation.message)
                return
            }
            else -> {}
        }

        // Валидация подтверждения пароля
        when (val confirmPasswordValidation = validator.validateConfirmPassword(password, confirmPassword)) {
            is ValidationResult.Error -> {
                _registerState.value = RegisterState.Error(confirmPasswordValidation.message)
                return
            }
            else -> {}
        }

        _registerState.value = RegisterState.Loading
        viewModelScope.launch {
            try {
                val success = authRepository.register(email, password)
                _registerState.value = if (success) {
                    RegisterState.Success
                } else {
                    RegisterState.Error(validator.getUserAlreadyExistsError())
                }
            } catch (e: Exception) {
                _registerState.value = RegisterState.Error(validator.getRegistrationError(e.message))
            }
        }
    }

    fun clearLoginState() {
        _loginState.value = LoginState.Idle
    }

    fun clearRegisterState() {
        _registerState.value = RegisterState.Idle
    }
}