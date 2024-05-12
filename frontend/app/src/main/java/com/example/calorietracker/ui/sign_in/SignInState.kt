package com.example.calorietracker.ui.sign_in

data class SignInUiState(
    val isSignInSuccessful: Boolean = false,
    val emailSignInError: Boolean = false,
    val signInError: String? = null,
    val email: String = "",
    val password: String = "",
    val emailErrorMessage: String? = null,
    val passwordErrorMessage: String? = null,
    val hasNetworkConnection: Boolean = true
)