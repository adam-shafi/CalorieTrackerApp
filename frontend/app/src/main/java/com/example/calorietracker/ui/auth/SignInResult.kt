package com.example.calorietracker.ui.auth
data class SignInResult(
    val data: UserData? = null,
    val errorMessage: String? = null
)

data class UserData(
    val userId: String,
    val username: String? = null,
    val email: String? = null,
    val profilePictureUrl: String? = null
)