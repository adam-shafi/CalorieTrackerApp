package com.example.calorietracker.ui.sign_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calorietracker.ui.auth.AuthUiClient
import com.example.calorietracker.ui.auth.SignInResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignInViewModel(val authUiClient: AuthUiClient) : ViewModel() {


    private val _signInState = MutableStateFlow(SignInState())
    val signInState = _signInState.asStateFlow()

    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState = _uiState.asStateFlow()

    fun updateUiState(
        newEmail: String = _uiState.value.email,
        newPassword: String = _uiState.value.password,
        newEmailError: String? = _uiState.value.emailErrorMessage,
        newPasswordError: String? = _uiState.value.passwordErrorMessage
    ) {
        _uiState.update {
            it.copy(
                email = newEmail,
                password = newPassword,
                emailErrorMessage = newEmailError,
                passwordErrorMessage = newPasswordError
            )
        }
    }

    fun onLoginClick() {
        if (validInput().not()) {
            return
        }
        viewModelScope.launch {
            onSignInResult(
                authUiClient.signInWithEmailAndPassword(
                    _uiState.value.email.trim(),
                    _uiState.value.password
                )
            )
        }
    }

    private fun validInput(): Boolean {
        var passed = true
        if (_uiState.value.email.isEmpty()) {
            updateUiState(newEmailError = "Please enter your email address.")
            passed = false
        }
        if (_uiState.value.password.isEmpty()) {
            updateUiState(newPasswordError = "Please enter your password.")
            passed = false
        }

        return passed
    }

    fun onSignInResult(result: SignInResult) {
        _signInState.update {
            it.copy(
                isSignInSuccessful = result.data != null,
                signInError = result.errorMessage
            )
        }
    }

    fun resetSignInState() {
        _signInState.update { SignInState() }
    }
}