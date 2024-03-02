package com.example.calorietracker.ui.sign_in

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null,
)

data class SignInUiState(
    val email: String = "",
    val password: String = "",
    val emailErrorMessage: String? = null,
    val passwordErrorMessage: String? = null
)