package com.example.calorietracker.ui.sign_in

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.example.calorietracker.ui.components.CustomOutlinedTextField
import com.example.calorietracker.ui.theme.dimen_8dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    viewModel: SignInViewModel,
    signInState: SignInState,
    onSignInSuccessful: () -> Unit,
    onBackClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onGoogleSignInClick: () -> Unit,
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    var passwordVisibility by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = signInState.signInError) {
        signInState.signInError?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    LaunchedEffect(key1 = signInState.isSignInSuccessful) {
        if (signInState.isSignInSuccessful) {
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            Column(
                modifier = Modifier.padding(dimen_8dp),
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
                    errorMessage = uiState.passwordErrorMessage,
                    trailingIcon = {
                        IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                            Icon(
                                imageVector = if (passwordVisibility) {
                                    ImageVector.vectorResource(R.drawable.visibility)
                                } else {
                                    ImageVector.vectorResource(R.drawable.visibility_off)
                                },
                                contentDescription = "Password Visibility Toggle"
                            )
                        }
                    }
                )
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { viewModel.onLoginClick() }) {
                    Text(text = "Log in")
                }
                TextButton(modifier = Modifier.fillMaxWidth(), onClick = { /*TODO*/ }) {
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
                    onClick = onGoogleSignInClick,
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

        }
    }


}