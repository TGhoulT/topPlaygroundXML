package com.example.topplaygroundxml.features.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.topplaygroundxml.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _loginState.value = LoginState.Error("Заполните все поля")
            return
        }

        if (!isValidEmail(email)) {
            _loginState.value = LoginState.Error("Введите корректный email адрес")
            return
        }

        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            try {
                val success = authRepository.login(email, password)
                _loginState.value = if (success) {
                    LoginState.Success
                } else {
                    LoginState.Error("Неверный email или пароль")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Ошибка при входе: ${e.message}")
            }
        }
    }

    fun register(email: String, password: String, confirmPassword: String) {
        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            _registerState.value = RegisterState.Error("Заполните все поля")
            return
        }

        // ВАЛИДАЦИЯ EMAIL
        if (!isValidEmail(email)) {
            _registerState.value = RegisterState.Error("Введите корректный email адрес")
            return
        }

        if (password != confirmPassword) {
            _registerState.value = RegisterState.Error("Пароли не совпадают")
            return
        }

        if (password.length < 6) {
            _registerState.value = RegisterState.Error("Пароль должен содержать минимум 6 символов")
            return
        }

        _registerState.value = RegisterState.Loading
        viewModelScope.launch {
            try {
                val success = authRepository.register(email, password)
                _registerState.value = if (success) {
                    RegisterState.Success
                } else {
                    RegisterState.Error("Пользователь с таким email уже существует")
                }
            } catch (e: Exception) {
                _registerState.value = RegisterState.Error("Ошибка при регистрации: ${e.message}")
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
        return email.matches(emailPattern.toRegex())
    }

    fun clearLoginState() {
        _loginState.value = LoginState.Idle
    }

    fun clearRegisterState() {
        _registerState.value = RegisterState.Idle
    }

    sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
        object Success : LoginState()
        data class Error(val message: String) : LoginState()
    }

    sealed class RegisterState {
        object Idle : RegisterState()
        object Loading : RegisterState()
        object Success : RegisterState()
        data class Error(val message: String) : RegisterState()
    }
}




