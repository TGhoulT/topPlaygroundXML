package com.example.topplaygroundxml.features.auth.presentation.viewmodel.state

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
}