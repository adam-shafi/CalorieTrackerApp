package com.example.calorietracker.ui.daily_log.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.calorietracker.R
import com.example.calorietracker.ui.components.VerticalText
import com.example.calorietracker.ui.daily_log.DailyLogUiState
import com.example.calorietracker.ui.daily_log.DerivedMacroInfoState
import com.example.calorietracker.ui.theme.dimen_8dp
import kotlin.math.roundToInt

@Composable
fun MacroCollapsedSnapshot(
    uiState: DailyLogUiState,
    calorieInfoState: DerivedMacroInfoState,
    proteinInfoState: DerivedMacroInfoState,
    carbInfoState: DerivedMacroInfoState,
    fatInfoState: DerivedMacroInfoState
) {
    MacroCollapsedSnapshot(
        calorieBudget = uiState.totalCalories.budget.roundToInt(),
        calorieEaten = uiState.totalCalories.foodEaten.roundToInt(),
        calorieRemainder = calorieInfoState.remainder,
        caloriePercent = calorieInfoState.percent,
        isCalorieOver = calorieInfoState.isOver,
        proteinEaten = uiState.totalProtein.foodEaten.roundToInt(),
        proteinBudget = uiState.totalProtein.budget.roundToInt(),
        proteinPercent = proteinInfoState.percent.roundToInt(),
        carbsEaten = uiState.totalCarbs.foodEaten.roundToInt(),
        carbsBudget = uiState.totalCarbs.budget.roundToInt(),
        carbPercent = carbInfoState.percent.roundToInt(),
        fatEaten = uiState.totalFat.foodEaten.roundToInt(),
        fatBudget = uiState.totalFat.budget.roundToInt(),
        fatPercent = fatInfoState.percent.roundToInt()
    )
}

@Composable
fun MacroCollapsedSnapshot(
    calorieBudget: Int,
    calorieEaten: Int,
    calorieRemainder: Float,
    caloriePercent: Float,
    isCalorieOver: Boolean,
    proteinEaten: Int,
    proteinBudget: Int,
    proteinPercent: Int,
    carbsEaten: Int,
    carbsBudget: Int,
    carbPercent: Int,
    fatEaten: Int,
    fatBudget: Int,
    fatPercent: Int
) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimen_8dp),
           horizontalArrangement =  Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LinearProgressIndicator(
                modifier = Modifier
                    .weight(2f)
                    .height(10.dp),
                progress = (caloriePercent / 100),
                trackColor = Color.White,
                strokeCap = StrokeCap.Round
            )
            VerticalText(
                modifier = Modifier.weight(1f),
                title = calorieRemainder.roundToInt().toString(),
                description = if (isCalorieOver) stringResource(id = R.string.over) else stringResource(
                    id = R.string.remaining
                ),
            )
        }
        Spacer(modifier = Modifier.height(dimen_8dp))

    }
}
