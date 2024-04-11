package com.example.calorietracker.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.calorietracker.R


@Composable
fun ServingSizeInput(
    amount: String,
    onAmountChange: (String) -> Unit,
    amountPlaceholder: String? = null,
    dropdownText: String,
    onDropdownTextChange: (String) -> Unit,
    dropdownItems: List<String>
) {
    var isDropdownExpanded by rememberSaveable { mutableStateOf(false) }
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier
                .weight(1f)
                .height(55.dp),
            value = amount,
            placeholder = {
                amountPlaceholder?.let {
                    Text(text = it)
                }
            },
            onValueChange = onAmountChange,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )
        )
        OutlinedButton(
            modifier = Modifier
                .weight(1f)
                .height(55.dp),
            onClick = { isDropdownExpanded = true },
            shape = RoundedCornerShape(10)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = dropdownText)
                Icon(
                    imageVector = if (isDropdownExpanded) Icons.Filled.ArrowDropDown else ImageVector.vectorResource(
                        R.drawable.baseline_arrow_drop_up_24
                    ),
                    contentDescription = null
                )
            }
            DropdownMenu(
                expanded = isDropdownExpanded,
                onDismissRequest = { isDropdownExpanded = false }) {
                dropdownItems.forEach {
                    DropdownMenuItem(text = { Text(text = it) },
                        onClick = {
                            onDropdownTextChange(it)
                            isDropdownExpanded = false
                        }
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
}