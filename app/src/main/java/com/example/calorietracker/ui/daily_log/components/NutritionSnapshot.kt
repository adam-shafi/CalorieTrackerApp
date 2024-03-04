package com.example.calorietracker.ui.daily_log.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calorietracker.R
import com.example.calorietracker.ui.components.CustomLinearProgressIndicator
import com.example.calorietracker.ui.daily_log.DailyLogUiState
import com.example.calorietracker.ui.daily_log.DerivedMacroInfoState
import com.example.calorietracker.ui.daily_log.MacroColorsState
import com.example.calorietracker.ui.theme.dimen_16dp
import com.example.calorietracker.ui.theme.dimen_8dp
import kotlin.math.roundToInt

@Composable
fun NutritionSnapshot(
    uiState: DailyLogUiState,
    macroColorsState: MacroColorsState,
    calorieInfoState: DerivedMacroInfoState,
    proteinInfoState: DerivedMacroInfoState,
    carbInfoState: DerivedMacroInfoState,
    fatInfoState: DerivedMacroInfoState
) {
    NutritionSnapshot(
        macroColorsState = macroColorsState,
        calorieFraction = "${uiState.totalCalories.foodEaten.roundToInt()} / ${uiState.totalCalories.budget.roundToInt()}",
        caloriePercent = calorieInfoState.percent,
        proteinFraction = "${uiState.totalProtein.foodEaten.roundToInt()} / ${uiState.totalProtein.budget.roundToInt()}",
        proteinPercent = proteinInfoState.percent,
        carbsFraction = "${uiState.totalCarbs.foodEaten.roundToInt()} / ${uiState.totalCarbs.budget.roundToInt()}",
        carbPercent = carbInfoState.percent,
        fatFraction = "${uiState.totalFat.foodEaten.roundToInt()} / ${uiState.totalFat.budget.roundToInt()}",
        fatPercent = fatInfoState.percent
    )
}


@Composable
fun NutritionSnapshot(
    macroColorsState: MacroColorsState,
    calorieFraction: String,
    caloriePercent: Float,
    proteinFraction: String,
    proteinPercent: Float,
    carbsFraction: String,
    carbPercent: Float,
    fatFraction: String,
    fatPercent: Float,
) {
    var fontSize by remember { mutableStateOf(16.sp) }
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
//        shape = RoundedCornerShape(0, 0, 25, 25)
    ) {
        Row(
            modifier = Modifier
                .padding(
                    start = dimen_8dp,
                    end = dimen_8dp,
                    top = dimen_8dp,
                    bottom = dimen_16dp
                ),
            horizontalArrangement = Arrangement.spacedBy(dimen_8dp)
        ) {
            NutritionInfo(
                modifier = Modifier.weight(1f),
                progress = caloriePercent,
                color = macroColorsState.calories,
                icon = ImageVector.vectorResource(R.drawable.calorie_16dp),
                fraction = calorieFraction,
                fontSize = fontSize,
                updateFontSize = { fontSize = it },
            )
            NutritionInfo(
                modifier = Modifier.weight(1f),
                progress = proteinPercent,
                color = macroColorsState.protein,
                title = "P",
                fraction = proteinFraction,
                fontSize = fontSize,
                updateFontSize = { fontSize = it },
            )
            NutritionInfo(
                modifier = Modifier.weight(1f),
                progress = fatPercent,
                color = macroColorsState.fat,
                title = "F",
                fraction = fatFraction,
                fontSize = fontSize,
                updateFontSize = { fontSize = it },
            )
            NutritionInfo(
                modifier = Modifier.weight(1f),
                progress = carbPercent,
                color = macroColorsState.carbs,
                title = "C",
                fraction = carbsFraction,
                fontSize = fontSize,
                updateFontSize = { fontSize = it },
            )
        }
    }
}

@Composable
fun NutritionInfo(
    modifier: Modifier = Modifier,
    progress: Float,
    color: Color,
    title: String? = null,
    icon: ImageVector? = null,
    fraction: String,
    fontSize: TextUnit,
    updateFontSize: (TextUnit) -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            icon?.let {
                Icon(imageVector = icon, contentDescription = null)
            }
            title?.let {
                Text(
                    text = title,
                    fontSize = fontSize,
                    onTextLayout = { result ->
                        fun constrain() {
                            updateFontSize(fontSize * 0.9f)
                        }
                        if (result.hasVisualOverflow) {
                            constrain()
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = fraction,
                maxLines = 1,
                fontSize = fontSize,
                onTextLayout = { result ->
                    fun constrain() {
                        updateFontSize(fontSize * 0.9f)
                    }
                    if (result.hasVisualOverflow) {
                        constrain()
                    }
                }
            )
        }
        CustomLinearProgressIndicator(
            progress = progress,
            color = color
        )
    }

}