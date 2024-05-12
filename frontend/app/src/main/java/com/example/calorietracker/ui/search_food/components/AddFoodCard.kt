package com.example.calorietracker.ui.search_food.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.calorietracker.ui.theme.dimen_8dp


@Composable
fun AddFoodCard(
    foodName: String,
    brandName: String,
    servingAmount: String,
    servingAmountUnits: String,
    calories: String,
    onAddClick: () -> Unit,
    imageVector: ImageVector
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = imageVector,
                tint = Color.Unspecified,
                contentDescription = "Chinese Food"
            )
            Spacer(modifier = Modifier.width(dimen_8dp))
            Column {
                Text(text = if (brandName.isNotBlank()) "$foodName ($brandName)" else foodName)
                Text(text = "$calories cal per $servingAmount $servingAmountUnits")
            }
        }
        IconButton(onClick = onAddClick) {
            Icon(imageVector = Icons.Filled.AddCircle, contentDescription = null)
        }
    }
}
