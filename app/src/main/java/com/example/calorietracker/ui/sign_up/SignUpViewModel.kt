package com.example.calorietracker.ui.sign_up

import android.app.Activity
import android.content.Context
import android.util.Patterns
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calorietracker.firestore.FirestoreUseCase
import com.example.calorietracker.ui.auth.AuthUiClient
import com.example.calorietracker.ui.auth.SignInResult
import com.example.calorietracker.util.Utility
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    val authUiClient: AuthUiClient,
    private val firestoreUseCase: FirestoreUseCase = FirestoreUseCase()
) : ViewModel() {
    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState = _uiState.asStateFlow()

    fun updateUiState(
        newEmail: String = _uiState.value.email,
        newUsername: String = _uiState.value.username,
        newPassword: String = _uiState.value.password,
        newConfirmPassword: String = _uiState.value.confirmPassword,
        newEmailError: String? = _uiState.value.emailErrorMessage,
        newUsernameError: String? = _uiState.value.usernameErrorMessage,
        newPasswordError: String? = _uiState.value.passwordErrorMessage,
        newConfirmPasswordError: String? = _uiState.value.confirmPasswordErrorMessage,
        newNetworkConnection: Boolean = _uiState.value.hasNetworkConnection,
        newEmailSignInError: Boolean = _uiState.value.emailSignInError,

        ) {
        _uiState.update {
            it.copy(
                email = newEmail,
                username = newUsername,
                password = newPassword,
                confirmPassword = newConfirmPassword,
                emailErrorMessage = newEmailError,
                usernameErrorMessage = newUsernameError,
                passwordErrorMessage = newPasswordError,
                confirmPasswordErrorMessage = newConfirmPasswordError,
                hasNetworkConnection = newNetworkConnection,
                emailSignInError = newEmailSignInError
            )
        }
    }


    fun onContinueWithGoogleClick(
        context: Context,
        launcher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>
    ) {
        if (Utility.isInternetAvailable(context).not()) {
            updateUiState(newNetworkConnection = false)
            return
        }
        viewModelScope.launch {
            val signInIntentSender = authUiClient.googleSignIn()
            launcher.launch(
                IntentSenderRequest.Builder(
                    signInIntentSender ?: return@launch
                ).build()
            )
        }
    }


    fun onSignUpClick(context: Context) {
        if (Utility.isInternetAvailable(context).not()) {
            updateUiState(newNetworkConnection = false)
            return
        }
        if (validInput().not()) {
            return
        }
        viewModelScope.launch {
            val signInResult = authUiClient.signUpWithDisplayNameEmailAndPassword(
                displayName = _uiState.value.username.trim(),
                email = _uiState.value.email.trim(),
                password = _uiState.value.password
            )
            onSignInResult(signInResult)
            signInResult.data?.let {
                firestoreUseCase.addUserDocument(
                    it.userId, hashMapOf(
                        "username" to (it.username ?: "username not found"),
                        "email" to (it.email ?: "email not found"),
                        "profile_picture_url" to (it.profilePictureUrl
                            ?: "profile picture not found")
                    )
                )
            }
        }
    }

    private fun validInput(): Boolean {
        var passed = true
        if (_uiState.value.email.isEmpty()) {
            updateUiState(newEmailError = "Please enter your email address.")
            passed = false
        } else if (Patterns.EMAIL_ADDRESS.matcher(_uiState.value.email).matches().not()) {
            updateUiState(newEmailError = "Please enter a valid email address.")
            passed = false
        }
        if (_uiState.value.username.isEmpty()) {
            updateUiState(newUsernameError = "Please enter your username.")
            passed = false
        }
        if (_uiState.value.password.isEmpty()) {
            updateUiState(newPasswordError = "Please enter your password.")
            passed = false
        } else if (_uiState.value.password.length < 6) {
            updateUiState(newPasswordError = "Password must be at least 6 characters.")
            passed = false
        }
        if (_uiState.value.password != _uiState.value.confirmPassword) {
            updateUiState(newConfirmPasswordError = "Passwords must match.")
            passed = false
        }
        return passed
    }

    private fun onSignInResult(result: SignInResult) {
        _uiState.update {
            it.copy(
                isSignInSuccessful = result.data != null,
                signInError = result.errorMessage
            )
        }
    }

    fun googleSignInWithIntent(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            viewModelScope.launch {
                val signInResult = authUiClient.googleSignInWithIntent(
                    intent = result.data ?: return@launch
                )
                onSignInResult(signInResult)
                signInResult.data?.let {
                    firestoreUseCase.addUserDocument(
                        it.userId, hashMapOf(
                            "username" to (it.username ?: "username not found"),
                            "email" to (it.email ?: "email not found"),
                            "profile_picture_url" to (it.profilePictureUrl
                                ?: "profile picture not found")
                        )
                    )
                }
            }
        }
    }

    fun resetSignInState() {
        _uiState.update {
            it.copy(
                isSignInSuccessful = false,
                signInError = null
            )
        }
    }
}