package com.example.calorietracker.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.sp

@Composable
fun NutritionFactsTextField(
    title: String,
    placeholder: String = "0 g",
    text: String,
    onValueChange: (String) -> Unit,
    tabs: Int = 0,
    isLast: Boolean = false,
    isError: Boolean = false,
    formatInput: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    LaunchedEffect(key1 = isFocused) {
        if(isFocused.not()) {
            formatInput()
        }
    }
    Column {
        Row(Modifier.fillMaxWidth()) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                prefix = {
                    Text(
                        text = title,
                        style = LocalTextStyle.current.copy(
                            textIndent = TextIndent(
                                firstLine = 32.sp.times(tabs)
                            )
                        )
                    )
                },
                value = text,
                placeholder = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = placeholder,
                        textAlign = TextAlign.End,
                        color = if(isError) Color.Red else Color.Unspecified
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = if(isLast) ImeAction.Done else ImeAction.Next
                ),
                onValueChange = onValueChange,
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                isError = isError,
                interactionSource = interactionSource
            )
        }
        if (isLast.not()) {
            Divider(modifier = Modifier.fillMaxWidth())
        }
    }
}