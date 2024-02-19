package com.example.calorietracker.ui.daily_log.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.calorietracker.R
import com.example.calorietracker.ui.components.VerticalText
import com.example.calorietracker.ui.daily_log.MealState
import com.example.calorietracker.ui.theme.CarbRed
import com.example.calorietracker.ui.theme.FatGreen
import com.example.calorietracker.ui.theme.ProteinBlue
import com.example.calorietracker.ui.theme.dimen_10dp
import com.example.calorietracker.ui.theme.dimen_16dp
import com.example.calorietracker.ui.theme.dimen_8dp
import kotlin.math.roundToInt

@Composable
fun MealCard(mealState: MealState) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimen_16dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(dimen_10dp),
            verticalArrangement = Arrangement.spacedBy(dimen_8dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = mealState.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(text = "${mealState.calories.roundToInt()} cal")
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                VerticalText(title = "${mealState.protein.roundToInt()} g", description = stringResource(id = R.string.protein), color = ProteinBlue)
                VerticalText(title = "${mealState.carbs.roundToInt()} g", description = stringResource(id = R.string.carbs), color = CarbRed)
                VerticalText(title = "${mealState.fat.roundToInt()} g", description = stringResource(id = R.string.fat), color = FatGreen)
            }
            mealState.foods.forEach{ food ->
                MealItem(
                    food = food.name,
                    serving = "${food.servingAmount.roundToInt()} ${food.servingUnits}",
                    calories = "${food.calories.roundToInt()} cal",
                    painter = painterResource(id = food.iconId)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = { /*TODO*/ }) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
                        Text("Add")
                    }
                }
            }

        }
    }
}


@Composable
fun MealItem(
    food: String,
    serving: String,
    calories: String,
    painter: Painter
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
                painter = painter,
                tint = Color.Unspecified,
                contentDescription = "Chinese Food"
            )
            Spacer(modifier = Modifier.width(dimen_8dp))
            Column {
                Text(text = food)
                Text(text = serving)
            }
        }
        Text(text = calories)
    }
}