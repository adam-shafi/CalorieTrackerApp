package com.example.calorietracker.ui.sign_up

data class SignUpUiState(
    val isSignInSuccessful: Boolean = false,
    val emailSignInError: Boolean = false,
    val signInError: String? = null,
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val emailErrorMessage: String? = null,
    val usernameErrorMessage: String? = null,
    val passwordErrorMessage: String? = null,
    val confirmPasswordErrorMessage: String? = null,
    val hasNetworkConnection: Boolean = true
)