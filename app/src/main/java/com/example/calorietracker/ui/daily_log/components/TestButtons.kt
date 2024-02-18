package com.example.calorietracker.ui.daily_log.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.calorietracker.ui.daily_log.Nutrition
import com.example.calorietracker.ui.theme.dimen_16dp

@Composable
fun IncrementMacrosTestButtons(increment: (Nutrition) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(onClick = { increment(Nutrition.Calories) }) {
            Text(text = "Eat Cal")
        }
        Button(onClick = { increment(Nutrition.Protein) }) {
            Text(text = "Eat Pro")
        }
        Button(onClick = { increment(Nutrition.Carbs) }) {
            Text(text = "Eat Carb")
        }
        Button(onClick = { increment(Nutrition.Fat) }) {
            Text(text = "Eat Fat")
        }
    }
}

@Composable
fun ModifyMealTestButtons(modify: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = dimen_16dp),
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { modify() }) {
            Text(text = "Modify Meal")
        }
    }
}