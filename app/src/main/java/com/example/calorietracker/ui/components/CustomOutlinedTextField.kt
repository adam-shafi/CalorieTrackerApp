package com.example.calorietracker.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CustomOutlinedTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null,
    errorMessage: String? = null
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = label) },
        value = value,
        onValueChange = onValueChange,
        trailingIcon = { trailingIcon?.invoke() },
        singleLine = true,
        isError = errorMessage != null,
        supportingText = {
            Text(text = errorMessage ?: "")
        }
    )
}