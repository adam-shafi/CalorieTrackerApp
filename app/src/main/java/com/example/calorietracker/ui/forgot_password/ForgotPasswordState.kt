package com.example.calorietracker.ui.forgot_password

data class ForgotPasswordState(
    val email: String = "",
    val emailErrorMessage: String? = null,
    val hasNetworkConnection: Boolean = true,
    val success: Boolean = false,
    val error: Boolean = false,
)