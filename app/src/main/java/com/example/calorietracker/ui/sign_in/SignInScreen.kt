package com.example.calorietracker.ui.sign_in

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.calorietracker.R
import com.example.calorietracker.ui.components.AddDialogObserver
import com.example.calorietracker.ui.components.CustomOutlinedTextField
import com.example.calorietracker.ui.theme.dimen_8dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    viewModel: SignInViewModel,
    onSignInSuccessful: () -> Unit,
    onBackClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            viewModel.googleSignInWithIntent(result)
        }
    )

    LaunchedEffect(key1 = uiState.signInError) {
        uiState.signInError?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
        viewModel.resetSignInState()
    }

    LaunchedEffect(key1 = uiState.isSignInSuccessful) {
        if (uiState.isSignInSuccessful) {
            Toast.makeText(
                context,
                "Log in successful",
                Toast.LENGTH_LONG
            ).show()
            onSignInSuccessful()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Log In") },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Navigate Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(dimen_8dp)
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimen_8dp)
        ) {
            CustomOutlinedTextField(
                label = "Email",
                value = uiState.email,
                onValueChange = {
                    viewModel.updateUiState(
                        newEmail = it,
                        newEmailError = null
                    )
                },
                leadingIcon = ImageVector.vectorResource(R.drawable.email),
                errorMessage = uiState.emailErrorMessage
            )
            CustomOutlinedTextField(
                label = "Password",
                value = uiState.password,
                onValueChange = {
                    viewModel.updateUiState(
                        newPassword = it,
                        newPasswordError = null
                    )
                },
                passwordVisibilityEnabled = true,
                errorMessage = uiState.passwordErrorMessage,
                leadingIcon = ImageVector.vectorResource(R.drawable.password),
            )
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { viewModel.onLoginClick(context) }) {
                Text(text = "Log in")
            }
            TextButton(modifier = Modifier.fillMaxWidth(), onClick = onForgotPasswordClick) {
                Text(text = "Forgot Password?")
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimen_8dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(modifier = Modifier.weight(.45f))
                Text(
                    modifier = Modifier.weight(.1f),
                    text = "OR",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
                Divider(modifier = Modifier.weight(.45f))
            }


            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { viewModel.onContinueWithGoogleClick(context, launcher) },
                colors = ButtonDefaults.outlinedButtonColors(),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.google),
                    contentDescription = "Google",
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(dimen_8dp))
                Text(text = "Continue with Google")
            }
            TextButton(onClick = onSignUpClick) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Don't have account?",
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.width(dimen_8dp))
                    Text(text = "Sign up")
                }
            }
        }
        AddDialogObserver(
            show = uiState.hasNetworkConnection.not(),
            onDismissRequest = {
                viewModel.updateUiState(newNetworkConnection = true)
            },
            icon = ImageVector.vectorResource(R.drawable.wifi_off),
            iconContentDescription = "Wifi Off",
            title = "No Internet",
            text = "Please check your connection status and try again"
        )

        AddDialogObserver(
            show = uiState.emailSignInError,
            onDismissRequest = {
                viewModel.updateUiState(newEmailSignInError = false)
            },
            icon = Icons.Filled.AccountCircle,
            iconContentDescription = "Account",
            title = "Login Failed",
            text = "The email or password you entered is incorrect. Please try again."
        )
    }
}



