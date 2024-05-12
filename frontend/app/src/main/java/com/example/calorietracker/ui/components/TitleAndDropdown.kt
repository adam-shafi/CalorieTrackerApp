package com.example.calorietracker.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calorietracker.R

@Composable
fun TitleAndDropdown(
    title: String,
    dropdownText: String,
    onDropdownTextChange: (String) -> Unit,
    dropdownItems: List<String>
) {
    var isDropdownExpanded by rememberSaveable { mutableStateOf(false) }

    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = title,
            fontSize = 16.sp
        )
        OutlinedButton(
            modifier = Modifier
                .weight(1f)
                .height(55.dp),
            onClick = {
                isDropdownExpanded = true
            },
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
                dropdownItems.forEach { text ->
                    DropdownMenuItem(text = { Text(text = text, fontSize = 16.sp) },
                        onClick = {
                            onDropdownTextChange(text)
                            isDropdownExpanded = false
                        }
                    )
                }
            }
        }

    }
    Spacer(modifier = Modifier.height(10.dp))
}