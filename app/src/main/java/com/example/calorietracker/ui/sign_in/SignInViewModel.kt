package com.example.calorietracker.ui.sign_in

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

class SignInViewModel(
    private val authUiClient: AuthUiClient,
    val firestoreUseCase: FirestoreUseCase = FirestoreUseCase()
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState = _uiState.asStateFlow()

    fun updateUiState(
        newEmail: String = _uiState.value.email,
        newPassword: String = _uiState.value.password,
        newEmailError: String? = _uiState.value.emailErrorMessage,
        newPasswordError: String? = _uiState.value.passwordErrorMessage,
        newNetworkConnection: Boolean = _uiState.value.hasNetworkConnection,
        newEmailSignInError: Boolean = _uiState.value.emailSignInError
    ) {
        _uiState.update {
            it.copy(
                email = newEmail,
                password = newPassword,
                emailErrorMessage = newEmailError,
                passwordErrorMessage = newPasswordError,
                hasNetworkConnection = newNetworkConnection,
                emailSignInError = newEmailSignInError
            )
        }
    }

    fun onLoginClick(context: Context) {
        if (Utility.isInternetAvailable(context).not()) {
            updateUiState(newNetworkConnection = false)
            return
        }
        if (validInput().not()) {
            return
        }
        viewModelScope.launch {
            val signInResult = authUiClient.signInWithEmailAndPassword(
                _uiState.value.email.trim(),
                _uiState.value.password
            )
            updateOnEmailSignInResult(signInResult)
            signInResult.data?.let {
                firestoreUseCase.addDocumentToFirestore(
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

    private fun validInput(): Boolean {
        var passed = true
        if (_uiState.value.email.isEmpty()) {
            updateUiState(newEmailError = "Please enter your email address.")
            passed = false
        } else if (Patterns.EMAIL_ADDRESS.matcher(_uiState.value.email).matches().not()) {
            updateUiState(newEmailError = "Please enter a valid email address.")
            passed = false
        }
        if (_uiState.value.password.isEmpty()) {
            updateUiState(newPasswordError = "Please enter your password.")
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

    private fun updateOnEmailSignInResult(result: SignInResult) {
        _uiState.update {
            it.copy(
                isSignInSuccessful = result.data != null,
                emailSignInError = result.data == null,
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
                    firestoreUseCase.addDocumentToFirestore(
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

    fun resetUiState() {
        _uiState.update {
            SignInUiState()
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