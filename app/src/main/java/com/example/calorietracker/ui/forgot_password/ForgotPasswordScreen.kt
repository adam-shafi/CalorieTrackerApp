package com.example.calorietracker.ui.forgot_password

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import com.example.calorietracker.R
import com.example.calorietracker.ui.components.AddDialogObserver
import com.example.calorietracker.ui.components.CustomOutlinedTextField
import com.example.calorietracker.ui.theme.dimen_8dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Forgot Password") },
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
                .padding(paddingValues)
                .padding(dimen_8dp)
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
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { viewModel.onResetClick(context)}
            ) {
                Text(text = "Reset Password")
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
            show = uiState.success,
            onDismissRequest = {
                viewModel.updateUiState(newSuccess = false)
                onBackClick()
            },
            icon = Icons.Filled.CheckCircle,
            iconContentDescription = "Success",
            title = "Successfully Sent Email!",
            text = "Please check your email for instructions on how to reset your password"
        )

        AddDialogObserver(
            show = uiState.error,
            onDismissRequest = {
                viewModel.updateUiState(newError = false)
            },
            icon = ImageVector.vectorResource(R.drawable.cancel),
            iconContentDescription = "Failure",
            title = "Error",
            text = "Something went wrong. Please try again."
        )
    }
}