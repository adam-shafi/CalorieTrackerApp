package com.example.calorietracker.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign

@Composable
fun AddDialogObserver(
    show: Boolean,
    onDismissRequest: () -> Unit,
    icon: ImageVector,
    iconContentDescription: String,
    title: String,
    text: String
) {
    if (show) {
        AlertDialog(
            icon = {
                Icon(
                    imageVector = icon,
                    contentDescription = iconContentDescription
                )
            },
            title = {
                Text(text = title)
            },
            text = {
                Text(
                    text = text,
                    textAlign = TextAlign.Center
                )
            },
            onDismissRequest = onDismissRequest,
            confirmButton = {
                TextButton(onClick = onDismissRequest) {
                    Text("OK")
                }
            }
        )
    }
}