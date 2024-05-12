package com.example.calorietracker.ui.forgot_password

import android.content.Context
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calorietracker.ui.auth.AuthUiClient
import com.example.calorietracker.util.Utility
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(private val authUiClient: AuthUiClient) : ViewModel() {
    private val _uiState = MutableStateFlow(ForgotPasswordState())
    val uiState = _uiState.asStateFlow()
    fun updateUiState(
        newEmail: String = _uiState.value.email,
        newEmailError: String? = _uiState.value.emailErrorMessage,
        newNetworkConnection: Boolean = _uiState.value.hasNetworkConnection,
        newSuccess: Boolean = _uiState.value.success,
        newError: Boolean = _uiState.value.error
    ) {
        _uiState.update {
            it.copy(
                email = newEmail,
                emailErrorMessage = newEmailError,
                hasNetworkConnection = newNetworkConnection,
                success = newSuccess,
                error = newError
            )
        }
    }
    fun onResetClick(context: Context) {
        if(Utility.isInternetAvailable(context).not()) {
            updateUiState(newNetworkConnection = false)
            return
        }
        if (validInput().not()) {
            return
        }
        viewModelScope.launch {
            val successful = authUiClient.sendPasswordResetEmail(_uiState.value.email.trim())
            updateUiState(
                newSuccess = successful,
                newError = successful.not()
            )


        }
    }

    private fun validInput(): Boolean {
        var passed = true
        if (_uiState.value.email.isEmpty()) {
            updateUiState(newEmailError = "Please enter your email address.")
            passed = false
        }
        else if(Patterns.EMAIL_ADDRESS.matcher(_uiState.value.email).matches().not()) {
            updateUiState(newEmailError = "Please enter a valid email address.")
            passed = false
        }
        return passed
    }
}