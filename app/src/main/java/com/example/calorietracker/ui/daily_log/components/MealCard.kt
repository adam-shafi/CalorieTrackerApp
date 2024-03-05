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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calorietracker.R
import com.example.calorietracker.ui.daily_log.MacroColorsState
import com.example.calorietracker.ui.daily_log.MealState
import com.example.calorietracker.ui.theme.dimen_10dp
import com.example.calorietracker.ui.theme.dimen_16dp
import com.example.calorietracker.ui.theme.dimen_8dp
import kotlin.math.roundToInt

@Composable
fun MealCard(
    mealState: MealState,
    onAddClick: (String) -> Unit,
    macroColorsState: MacroColorsState
) {
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
//                Row(
//                    horizontalArrangement = Arrangement.spacedBy(dimen_10dp)
//                ) {
//                    Text(
//                        text = "P ${mealState.protein.roundToInt()}g",
//                        color = WildBlue
//                    )
//                    Text(
//                        text = "F ${mealState.fat.roundToInt()}g",
//                        color = ZestyGreen
//                    )
//                    Text(
//                        text = "C ${mealState.carbs.roundToInt()}g",
//                        color = FruityRed
//                    )
//                    Text(
//                        text = "${mealState.calories.roundToInt()} cal",
//                        color = BrandOrange
//                    )
//                }


            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MealNutritionInfo(
                    modifier = Modifier.weight(1f),
                    title = "${mealState.calories.roundToInt()}",
                    description = "Calories",
                    color = macroColorsState.calories,
                    icon = ImageVector.vectorResource(R.drawable.calorie)
                )
                MealNutritionInfo(
                    modifier = Modifier.weight(1f),
                    title = "${mealState.protein.roundToInt()}g",
                    description = stringResource(id = R.string.protein),
                    color = macroColorsState.protein,
                    icon = ImageVector.vectorResource(R.drawable.protein)
                )

                MealNutritionInfo(
                    modifier = Modifier.weight(1f),
                    title = "${mealState.fat.roundToInt()}g",
                    description = stringResource(id = R.string.fat),
                    color = macroColorsState.fat,
                    icon = ImageVector.vectorResource(R.drawable.fat)
                )
                MealNutritionInfo(
                    modifier = Modifier.weight(1f),
                    title = "${mealState.carbs.roundToInt()}g",
                    description = stringResource(id = R.string.carbs),
                    color = macroColorsState.carbs,
                    icon = ImageVector.vectorResource(R.drawable.carbs)
                )

            }
            mealState.foods.forEach { food ->
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
                Button(onClick = { onAddClick(mealState.name) }) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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

@Composable
fun MealNutritionInfo(
    modifier: Modifier = Modifier,
    title: String,
    titleFont: TextUnit = 18.sp,
    description: String,
    color: Color,
    icon: ImageVector
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = title, fontSize = titleFont)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
//            Icon(imageVector = icon, contentDescription = null, tint = color)
//            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = description,
                color = color
            )
        }

    }
}