package com.example.calorietracker.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.calorietracker.R

@Composable
fun CustomOutlinedTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    passwordVisibilityEnabled: Boolean = false,
    errorMessage: String? = null,
    leadingIcon: ImageVector? = null
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = label) },
        value = value,
        leadingIcon = {
            leadingIcon?.let {
                Icon(imageVector = leadingIcon, contentDescription = null)
            }
        },
        onValueChange = onValueChange,
        visualTransformation = if (passwordVisible.not() && passwordVisibilityEnabled) PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = {
            if (passwordVisibilityEnabled) {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) {
                            ImageVector.vectorResource(R.drawable.visibility)
                        } else {
                            ImageVector.vectorResource(R.drawable.visibility_off)
                        },
                        contentDescription = "Password Visibility Toggle"
                    )
                }
            }
        },
        singleLine = true,
        isError = errorMessage != null,
        supportingText = {
            Text(text = errorMessage ?: "")
        }
    )
}